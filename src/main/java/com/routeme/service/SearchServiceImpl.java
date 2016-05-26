package com.routeme.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.routeme.model.NonTransitRoute;
import com.routeme.model.NonTransitStep;
import com.routeme.model.Route;
import com.routeme.model.Step;
import com.routeme.model.TransitRoute;
import com.routeme.model.TransitStep;
import com.routeme.predictionio.PredictionIOClient;
import com.routeme.utility.directions.GoogleDirectionsUtility;
import com.routeme.utility.directions.RouteParseException;

@Service
public class SearchServiceImpl implements SearchService {
    private PredictionIOClient predictionIOClient;

    @Override
    public SearchResponseDTO search(String originInput, String destinationInput) {
        predictionIOClient = new PredictionIOClient();
        String origin = originInput;
        String destination = destinationInput;
        SearchResponseDTO searchResponseDTO = null;
        GeoApiContext context = new GeoApiContext().setApiKey(GoogleDirectionsUtility.getGoogleDirectionsApiKey());
        try {
            List<RouteDTO> allRouteDTOResults = new ArrayList<RouteDTO>();
            DirectionsResult transitResult = DirectionsApi.newRequest(context).origin(origin).destination(destination)
                    .mode(TravelMode.TRANSIT).transitMode(TransitMode.RAIL, TransitMode.BUS).alternatives(true)
                    .region("de").await();
            allRouteDTOResults.addAll(convertGoogleTransitResultToSearchResponseDTO(transitResult));

            DirectionsResult bicyclingResults = DirectionsApi.newRequest(context).origin(origin)
                    .destination(destination).alternatives(true).region("de").mode(TravelMode.BICYCLING).await();
            allRouteDTOResults.addAll(convertGoogleNonTransitResultToSearchResponseDTO(bicyclingResults));

            DirectionsResult drivingResults = DirectionsApi.newRequest(context).origin(origin).destination(destination)
                    .alternatives(true).region("de").mode(TravelMode.DRIVING).await();
            allRouteDTOResults.addAll(convertGoogleNonTransitResultToSearchResponseDTO(drivingResults));

            DirectionsResult walkingResults = DirectionsApi.newRequest(context).origin(origin).destination(destination)
                    .alternatives(true).region("de").mode(TravelMode.WALKING).await();
            allRouteDTOResults.addAll(convertGoogleNonTransitResultToSearchResponseDTO(walkingResults));

            // TODO Add routes to client
            // predictionIOClient.addRoutesToClient(allRouteDTOResults);
            predictionIOClient.closeEventClient();
            // TODO Set each route types according to all results (least time,
            // least changes..)
            setRouteTypes(allRouteDTOResults);

            // TODO Send the DTO to PIO to sort it and then return it here!
            // PredictionIOClient.recommendRoutes(listOfRoutes, userId);

            // Return final recommendation!
            searchResponseDTO = convertRouteDTOsToSearchResponseDTO(allRouteDTOResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchResponseDTO;
    }

    private void setRouteTypes(List<RouteDTO> allResults) {
        // TODO: But for which transit or other travel modes?!
    }

    private SearchResponseDTO convertRouteDTOsToSearchResponseDTO(List<RouteDTO> routeDTOs) {
        SearchResponseDTO searchResponseDTO = new SearchResponseDTO();
        searchResponseDTO.setRouteResults(routeDTOs);
        return searchResponseDTO;
    }

    private List<RouteDTO> convertGoogleTransitResultToSearchResponseDTO(DirectionsResult directionResult) {
        List<RouteDTO> routeDTOs = new ArrayList<RouteDTO>();
        for (int i = 0; i < directionResult.routes.length; i++) {
            TransitRoute route;
            try {
                route = new TransitRoute(directionResult.routes[i]);
                routeDTOs.add(convertRouteToRouteDTO(route));
            } catch (RouteParseException e) {
                Logger.getRootLogger().info(e.getMessage());
            }
        }
        return routeDTOs;
    }

    private List<RouteDTO> convertGoogleNonTransitResultToSearchResponseDTO(DirectionsResult directionResult) {
        List<RouteDTO> routeDTOs = new ArrayList<RouteDTO>();
        for (int i = 0; i < directionResult.routes.length; i++) {
            NonTransitRoute route = new NonTransitRoute(directionResult.routes[i]);
            routeDTOs.add(convertRouteToRouteDTO(route));
        }
        return routeDTOs;
    }

    private RouteDTO convertRouteToRouteDTO(Route route) {
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setDistance(route.getDistance().humanReadable);
        routeDTO.setDuration(route.getDuration().humanReadable);
        routeDTO.setEndAddress(route.getEndAddress());
        routeDTO.setStartAddress(route.getStartAddress());
        routeDTO.setOverviewPolyLine(route.getOverviewPolyLine().getEncodedPath());
        routeDTO.setPredictionIoId(route.getPredictionIoId());
        routeDTO.setTransportationModes(route.getTransportationModes());
        setRouteDTOStepDTOs(routeDTO, route);
        if (route instanceof TransitRoute) {
            DateFormat timeFormat = new SimpleDateFormat("KK:mm a");
            routeDTO.setArrivalTime(timeFormat.format(route.getArrivalTime().toDate()));
            routeDTO.setDepartureTime(timeFormat.format(route.getDepartureTime().toDate()));
            routeDTO.setRouteSummary(routeDTO.getDepartureTime() + "-" + routeDTO.getArrivalTime() + " ("
                    + routeDTO.getDuration() + ")");
            routeDTO.setTransit(true);
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
            stepDTO = new TransitStepDTO(transitStep.getTransportationMode(),
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
