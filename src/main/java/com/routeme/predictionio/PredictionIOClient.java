package com.routeme.predictionio;

import io.prediction.EventClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.routeme.AppConfig;
import com.routeme.dto.RouteDTO;
import com.routeme.model.DummyRoute;

public class PredictionIOClient {
    private final String accessKey = "eJErhxNseKZCZ8ijxy0fLjtj8jeHvb2ngMLoRIpwPGr5inugcbGHGSAgvJM1ZqLs";
    public static final String PREFERENCE_ROUTE_TYPE_LEASTTIME = "leastTime";
    public static final String PREFERENCE_ROUTE_TYPE_LEASTCHANGES = "leastChanges";
    public static final String PREFERENCE_ROUTE_MODE_BUS = "Bus";
    public static final String PREFERENCE_ROUTE_MODE_UBAHN = "U-bahn";
    private EventClient eventClient;
    Random rand = new Random();
    Map<String, Object> emptyProperty = ImmutableMap.of();

    public PredictionIOClient() {
        eventClient = new EventClient(accessKey);
    }

    public void closeEventClient() {
        eventClient.close();
    }

    public EventClient getEventClient() {
        return eventClient;
    }

    public void setEventClient(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    public void takeRoute(String userEmail, String routeDescription) {
        triggerEvent(userEmail, "take", routeDescription);
    }

    public void preferRouteType(String userEmail, String routeType) {
        triggerEvent(userEmail, "route-preference", routeType);
    }

    public void preferRouteMode(String userEmail, String routeMode) {
        triggerEvent(userEmail, "mode-preference", routeMode);
    }

    public void viewRoute(String userEmail, String routeId) {
        triggerEvent(userEmail, "view", routeId);
    }

    public void viewRouteLast(String userEmail, String routeId) {
        triggerEvent(userEmail, "view-last", routeId);
    }

    public void triggerEvent(String userEmail, String eventName, String targetValue) {
        System.out.println("User " + userEmail + eventName + " " + targetValue);
        try {
            if (eventName.equals("like") || eventName.equals("view") || eventName.equals("view-last")) {
                addRouteItem(eventName, targetValue);
            }
            eventClient.userActionItem(eventName, userEmail, targetValue, emptyProperty);
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to trigger event: " + eventName + " to client because of " + e.getMessage());
        }
    }

    public void addRouteItem(String eventName, String routePioId) {
        Map<String, Object> properties = new HashMap<String, Object>();
        ArrayList<String> routeId = new ArrayList<String>();
        routeId.add(routePioId);
        properties.put("routeId", routeId);
        try {
            eventClient.setItem(routePioId, properties);
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to add route " + routePioId + " to client because of " + e.getMessage());
        }
    }

    public void addUserToClient(String userEmail) {
        System.out.println("Add user " + userEmail);
        try {
            eventClient.setUser(userEmail, emptyProperty);
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to add user " + userEmail + " to client because of " + e.getMessage());
        }
    }

    public void addRouteToClient(DummyRoute route) {
        String routeUniqueId = route.getRouteUniqueId();
        System.out.println("Add route:\n " + routeUniqueId);
        try {
            eventClient.setItem("" + routeUniqueId, route.getRoutePIOProperties());
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to add route " + route.getId() + " to client because of " + e.getMessage());
        }
    }

    public void recommendRoutes(String userEmail, List<RouteDTO> routes) {
        List<String> searchResultsRouteIds = new ArrayList<String>();
        for (RouteDTO route : routes) {
            searchResultsRouteIds.add(route.getPredictionIoId());
        }
        Map<String, Object> fieldsArray = new HashMap<String, Object>();
        fieldsArray.put("name", "routeId");
        fieldsArray.put("values", searchResultsRouteIds);
        fieldsArray.put("bias", -1);
        try {
            JsonObject response = AppConfig.engineClient.sendQuery(ImmutableMap.<String, Object> of("user", userEmail,
                    "fields", fieldsArray));
            System.out.println(response.getAsString());
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to query for routes to client because of " + e.getMessage());
        }
    }

    public void addRoutesToClient(List<RouteDTO> allResults) {
        for (int i = 0; i < allResults.size(); i++) {
            RouteDTO routeDTO = allResults.get(i);
            System.out.println("Add route " + routeDTO.getPredictionIoId() + " to event store");
            try {
                // TODO: Fix this empty string param
                eventClient.setItem(routeDTO.getPredictionIoId(), routeDTO.getRoutePIOProperties(""));
                triggerEvent("trainer", "take", routeDTO.getPredictionIoId());
            } catch (ExecutionException | InterruptedException | IOException e) {
                System.out.println("Failed to add route " + routeDTO.getPredictionIoId() + " to client because of "
                        + e.getMessage());
            }
        }
    }

    /**
     * Example of a batch import
     * 
     * @param numberOfUsers
     * @param numberOfRoutes
     */
    public void randomRouteTakes(int numberOfUsers, int numberOfRoutes) {
        for (int user = 1; user <= numberOfUsers; user++) {
            for (int routeCounter = 1; routeCounter <= numberOfRoutes; routeCounter++) {
                int route = rand.nextInt(numberOfRoutes) + 1;
                System.out.println("User " + user + " takes route " + route);
                try {
                    eventClient.userActionItem("take", "" + user, "" + route, emptyProperty);
                } catch (ExecutionException | InterruptedException | IOException e) {
                    System.out.println("User " + user + "failed to take route " + route + " because of "
                            + e.getMessage());
                }
            }
        }
    }

}
