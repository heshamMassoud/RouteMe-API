package com.routeme.utility.directions;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Vehicle;
import com.routeme.model.NonTransitRoute;
import com.routeme.model.Route;
import com.routeme.model.TransitRoute;

public class GoogleDirectionsUtility {
    protected static final String MUNICH_UNDERGROUND = "U-bahn";
    protected static final String MUNICH_COMMUTER_RAIL = "S-bahn";
    protected static final String MUNICH_TRAM_RAIL = "tram";
    protected static final String MUNICH_BUS_ = "Bus";
    protected static final String GOOGLE_UNDERGROUND = "Subway";
    protected static final String GOOGLE_COMMUTER_RAIL = "Commuter train";
    protected static final String GOOGLE_TRAM_RAIL = "Tram";
    protected static final String GOOGLE_BUS_ = "Bus";
    protected static final String GOOGLE_DIRECTIONS_API_KEY = "AIzaSyBgegEmbuKzGU4RKWWqqUu8y6ra2a87ZtY";

    public static String getMunichTransitVehicleName(Vehicle googleVehicle) {
        String googleVehicleName = googleVehicle.name;
        switch (googleVehicleName) {
        case GOOGLE_UNDERGROUND:
            return MUNICH_UNDERGROUND;
        case GOOGLE_COMMUTER_RAIL:
            return MUNICH_COMMUTER_RAIL;
        default:
            return googleVehicleName;
        }
    }

    public static void printRouteResults(DirectionsResult result, TravelMode travelMode) {
        System.out.println("--");
        for (int i = 0; i < result.routes.length; i++) {
            Route route;
            if (travelMode == TravelMode.TRANSIT) {
                route = new TransitRoute(result.routes[i]);
            } else {
                route = new NonTransitRoute(result.routes[i]);
            }
            System.out.println(route.getPredictionIoId());
        }
    }

    public static String getGoogleDirectionsApiKey() {
        return GOOGLE_DIRECTIONS_API_KEY;
    }
}
