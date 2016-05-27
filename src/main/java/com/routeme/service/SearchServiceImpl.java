package com.routeme.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TravelMode;
import com.routeme.dto.NonTransitStepDTO;
import com.routeme.dto.RouteDTO;
import com.routeme.dto.SearchResponseDTO;
import com.routeme.dto.StepDTO;
import com.routeme.dto.TransitStepDTO;
import com.routeme.dto.UserDTO;
import com.routeme.model.NonTransitRoute;
import com.routeme.model.NonTransitStep;
import com.routeme.model.Route;
import com.routeme.model.Step;
import com.routeme.model.TransitRoute;
import com.routeme.model.TransitStep;
import com.routeme.model.Util;
import com.routeme.predictionio.PredictionIOClient;
import com.routeme.utility.directions.GoogleDirectionsUtility;
import com.routeme.utility.directions.RouteParseException;

@Service
public class SearchServiceImpl implements SearchService {
    private PredictionIOClient predictionIOClient;
    private UserService service;
    private int numberOfLikes;

    @Autowired
    public SearchServiceImpl(UserService service) {
        this.service = service;
        predictionIOClient = new PredictionIOClient();
    }

    @Override
    public SearchResponseDTO search(String originInput, String destinationInput, String userId) {
        numberOfLikes = 0;
        String origin = originInput;
        String destination = destinationInput;
        UserDTO userDTO = service.findById(userId);
        ArrayList<String> likedRoutesPioIds = userDTO.getLikedRoutes();
        ArrayList<String> travelModePreference = userDTO.getTravelModePreference();
        ArrayList<String> routeTypePreference = userDTO.getRouteTypePreference();

        SearchResponseDTO searchResponseDTO = null;
        GeoApiContext context = new GeoApiContext().setApiKey(GoogleDirectionsUtility.getGoogleDirectionsApiKey());
        try {
            List<RouteDTO> allRouteDTOResults = new ArrayList<RouteDTO>();
            DirectionsResult transitResult = DirectionsApi.newRequest(context).origin(origin).destination(destination)
                    .mode(TravelMode.TRANSIT).transitMode(TransitMode.RAIL, TransitMode.BUS).alternatives(true)
                    .region("de").await();
            allRouteDTOResults.addAll(convertGoogleTransitResultToSearchResponseDTO(transitResult, likedRoutesPioIds));

            DirectionsResult bicyclingResults = DirectionsApi.newRequest(context).origin(origin)
                    .destination(destination).alternatives(true).region("de").mode(TravelMode.BICYCLING).await();
            allRouteDTOResults.addAll(convertGoogleNonTransitResultToSearchResponseDTO(bicyclingResults,
                    likedRoutesPioIds));

            DirectionsResult drivingResults = DirectionsApi.newRequest(context).origin(origin).destination(destination)
                    .alternatives(true).region("de").mode(TravelMode.DRIVING).await();
            allRouteDTOResults.addAll(convertGoogleNonTransitResultToSearchResponseDTO(drivingResults,
                    likedRoutesPioIds));

            DirectionsResult walkingResults = DirectionsApi.newRequest(context).origin(origin).destination(destination)
                    .alternatives(true).region("de").mode(TravelMode.WALKING).await();
            allRouteDTOResults.addAll(convertGoogleNonTransitResultToSearchResponseDTO(walkingResults,
                    likedRoutesPioIds));

            // TODO Add routes to client
            // predictionIOClient.addRoutesToClient(allRouteDTOResults);
            predictionIOClient.closeEventClient();

            Collections.sort(allRouteDTOResults, new RoutePreferenceSorter(travelModePreference, routeTypePreference));

            // TODO Send the DTO to PIO to sort it and then return it here!
            // PredictionIOClient.recommendRoutes(listOfRoutes, userId);
            explainRecommendation(allRouteDTOResults);

            // Return final recommendation!
            searchResponseDTO = convertRouteDTOsToSearchResponseDTO(allRouteDTOResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResponseDTO;
    }

    private void explainRecommendation(List<RouteDTO> allRouteDTOResults) {
        // TODO: check PIO recommender scores if 0 or some score then provide
        // appropriate explanation else..
        for (int i = 0; i < allRouteDTOResults.size(); i++) {
            RouteDTO routeDTO = allRouteDTOResults.get(i);
            if (routeDTO.isLiked() && numberOfLikes > 1) {
                routeDTO.setExplanations(Util.Explanations.LIKES_PREFERENCES);
            } else if (routeDTO.isLiked()) {
                routeDTO.setExplanations(Util.Explanations.LIKED);
            } else {
                routeDTO.setExplanations(Util.Explanations.PREFERENCES);
            }
        }
    }

    private SearchResponseDTO convertRouteDTOsToSearchResponseDTO(List<RouteDTO> routeDTOs) {
        SearchResponseDTO searchResponseDTO = new SearchResponseDTO();
        searchResponseDTO.setRouteResults(routeDTOs);
        return searchResponseDTO;
    }

    private List<RouteDTO> convertGoogleTransitResultToSearchResponseDTO(DirectionsResult directionResult,
            ArrayList<String> likeRoutesPioIds) {
        List<RouteDTO> routeDTOs = new ArrayList<RouteDTO>();
        int leastNumberOfChanges = Integer.MAX_VALUE;
        long leastDurationInSeconds = Integer.MAX_VALUE;
        int leastChangesRouteDTOIndex = 0;
        int leastDurationRouteDTOIndex = 0;
        for (int i = 0; i < directionResult.routes.length; i++) {
            TransitRoute route;
            try {
                route = new TransitRoute(directionResult.routes[i]);
                int routeNumberOfChanges = route.getNumberOfChanges();
                long routeDurationInSeconds = route.getDuration().inSeconds;
                if (routeNumberOfChanges < leastNumberOfChanges) {
                    leastNumberOfChanges = routeNumberOfChanges;
                    leastChangesRouteDTOIndex = i;
                }
                if (routeDurationInSeconds < leastDurationInSeconds) {
                    leastDurationInSeconds = routeDurationInSeconds;
                    leastDurationRouteDTOIndex = i;
                }
                routeDTOs.add(convertRouteToRouteDTO(route, likeRoutesPioIds));
            } catch (RouteParseException e) {
                Logger.getRootLogger().info(e.getMessage());
            }
        }
        routeDTOs.get(leastChangesRouteDTOIndex).setLeastChangesRoute(true);
        routeDTOs.get(leastDurationRouteDTOIndex).setLeastDurationRoute(true);
        return routeDTOs;
    }

    private List<RouteDTO> convertGoogleNonTransitResultToSearchResponseDTO(DirectionsResult directionResult,
            ArrayList<String> likeRoutesPioIds) {
        List<RouteDTO> routeDTOs = new ArrayList<RouteDTO>();
        for (int i = 0; i < directionResult.routes.length; i++) {
            NonTransitRoute route = new NonTransitRoute(directionResult.routes[i]);
            routeDTOs.add(convertRouteToRouteDTO(route, likeRoutesPioIds));
        }
        return routeDTOs;
    }

    private RouteDTO convertRouteToRouteDTO(Route route, ArrayList<String> likeRoutesPioIds) {
        RouteDTO routeDTO = new RouteDTO();
        String routePioId = route.getPredictionIoId();
        routeDTO.setPredictionIoId(routePioId);
        if (likeRoutesPioIds.contains(routePioId)) {
            numberOfLikes++;
            routeDTO.setLiked(true);
        }
        routeDTO.setStartLocationLat(route.getStartLocationLat());
        routeDTO.setStartLocationLng(route.getStartLocationLng());
        routeDTO.setEndLocationLat(route.getEndLocationLat());
        routeDTO.setEndLocationLng(route.getEndLocationLng());
        routeDTO.setDistance(route.getDistance().humanReadable);
        routeDTO.setDuration(route.getDuration().humanReadable);
        routeDTO.setEndAddress(route.getEndAddress());
        routeDTO.setStartAddress(route.getStartAddress());
        routeDTO.setOverviewPolyLine(route.getOverviewPolyLine().getEncodedPath());
        routeDTO.setTransportationModes(route.getTransportationModes());
        setRouteDTOStepDTOs(routeDTO, route);
        if (route instanceof TransitRoute) {
            routeDTO.setArrivalTime(Util.Route.TIME_FORMAT.format(route.getArrivalTime().toDate()));
            routeDTO.setDepartureTime(Util.Route.TIME_FORMAT.format(route.getDepartureTime().toDate()));
            routeDTO.setDepartureDateTimeInMillis(route.getDepartureTime().getMillis());
            routeDTO.setRouteSummary(routeDTO.getDepartureTime() + "-" + routeDTO.getArrivalTime() + " ("
                    + routeDTO.getDuration() + ")");
            routeDTO.setTransit(true);
            routeDTO.setDurationInSeconds(route.getDuration().inSeconds);
            routeDTO.setNumberOfChanges(((TransitRoute) route).getNumberOfChanges());
        } else {
            routeDTO.setRouteSummary(route.getRouteSummary() + " (" + routeDTO.getDistance() + ")");
            routeDTO.setTransit(false);
        }
        return routeDTO;
    }

    private void setRouteDTOStepDTOs(RouteDTO routeDTO, Route route) {
        ArrayList<StepDTO> stepDTOs = new ArrayList<StepDTO>();
        for (Step step : route.getStepsData()) {
            StepDTO stepDTO = convertStepToStepDTO(step);
            stepDTOs.add(stepDTO);
        }
        routeDTO.setSteps(stepDTOs);
    }

    private StepDTO convertStepToStepDTO(Step step) {
        StepDTO stepDTO = null;
        if (step instanceof TransitStep) {
            TransitStep transitStep = (TransitStep) step;
            String statTime = Util.Route.TIME_FORMAT.format(transitStep.getStartTime().toDate());
            String endTime = Util.Route.TIME_FORMAT.format(transitStep.getEndTime().toDate());
            stepDTO = new TransitStepDTO(transitStep.getTransportationMode(), transitStep.getStartStation(),
                    transitStep.getEndStation(), transitStep.getDuration(), statTime, endTime,
                    transitStep.getTransportationVehicleShortName(), transitStep.getTransportationLineColorCode(),
                    transitStep.getTransportationLineHeadSign());
        } else {
            NonTransitStep nonTransitStep = (NonTransitStep) step;
            stepDTO = new NonTransitStepDTO(nonTransitStep.getTransportationMode(), nonTransitStep.getHtmlIntruction(),
                    nonTransitStep.getDistance());
        }
        return stepDTO;
    }
}

class RoutePreferenceSorter implements Comparator<RouteDTO> {
    ArrayList<String> travelModePreference = new ArrayList<String>();
    ArrayList<String> routeTypePreference = new ArrayList<String>();

    public RoutePreferenceSorter(ArrayList<String> travelModePreference, ArrayList<String> routeTypePreference) {
        this.travelModePreference = travelModePreference;
        this.routeTypePreference = routeTypePreference;
    }

    @Override
    public int compare(RouteDTO routeDTO1, RouteDTO routeDTO2) {
        int likedRouteScore = compareWithRespectToLikes(routeDTO1, routeDTO2);
        if (likedRouteScore == 0) {
            int travelModePrefScore = compareWithRespectToTravelModePreference(routeDTO1, routeDTO2);
            if (travelModePrefScore == 0) {
                return compareWithRespectToRouteTypePreferences(routeDTO1, routeDTO2);
            } else {
                return travelModePrefScore;
            }
        } else {
            return likedRouteScore;
        }
    }

    private int compareWithRespectToLikes(RouteDTO routeDTO1, RouteDTO routeDTO2) {
        if (routeDTO1.isLiked() == routeDTO2.isLiked()) {
            return 0;
        } else {
            if (routeDTO1.isLiked()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private int compareWithRespectToTravelModePreference(RouteDTO routeDTO1, RouteDTO routeDTO2) {
        ArrayList<String> routeDTO1Modes = routeDTO1.getTransportationModes();
        ArrayList<String> routeDTO2Modes = routeDTO2.getTransportationModes();
        int totalTransportationModesScore = 0;
        for (String transportationMode1 : routeDTO1Modes) {
            int transportationMode1Score = travelModePreference.indexOf(transportationMode1);
            for (String transportationMode2 : routeDTO2Modes) {
                int transportationMode2Score = travelModePreference.indexOf(transportationMode2);
                totalTransportationModesScore += transportationMode1Score - transportationMode2Score;
            }
        }
        return totalTransportationModesScore;
    }

    private int compareWithRespectToRouteTypePreferences(RouteDTO routeDTO1, RouteDTO routeDTO2) {
        int leastTimeScore = (int) (routeDTO1.getDurationInSeconds() - routeDTO2.getDurationInSeconds());
        int leastNumberOfChangesScore = routeDTO1.getNumberOfChanges() - routeDTO2.getNumberOfChanges();
        int soonestScore = (int) (routeDTO2.getDepartureDateTimeInMillis() - routeDTO1.getDepartureDateTimeInMillis());
        int highPrefScore = 0;
        int lowPrefScore = 0;
        switch (routeTypePreference.get(0)) {
        case Util.Route.LEAST_TIME_PREFERENCE:
            highPrefScore = leastTimeScore;
            lowPrefScore = leastNumberOfChangesScore;
            break;
        case Util.Route.LEAST_CHANGES_PREFERENCE:
            highPrefScore = leastNumberOfChangesScore;
            lowPrefScore = leastTimeScore;
            break;
        }
        return compareWithRespectToThreePrefScores(highPrefScore, lowPrefScore, soonestScore);
    }

    private int compareWithRespectToThreePrefScores(int highPrefScore, int lowPrefScore, int lowestPrefScore) {
        if (highPrefScore == 0) {
            if (lowPrefScore == 0) {
                return lowestPrefScore;
            }
            return lowPrefScore;
        }
        return highPrefScore;
    }

}
