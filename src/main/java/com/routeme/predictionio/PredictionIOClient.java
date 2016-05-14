package com.routeme.predictionio;

import io.prediction.EventClient;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;
import com.routeme.model.Route;

public class PredictionIOClient {
    private final String accessKey = "eJErhxNseKZCZ8ijxy0fLjtj8jeHvb2ngMLoRIpwPGr5inugcbGHGSAgvJM1ZqLs";
    public static final String PREFERENCE_ROUTE_TYPE_LEASTTIME = "leastTime";
    public static final String PREFERENCE_ROUTE_TYPE_LEASTCHANGES = "leastChanges";
    public static final String PREFERENCE_ROUTE_MODE_BUS = "bus";
    public static final String PREFERENCE_ROUTE_MODE_UBAHN = "ubahn";
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

    public void takeRoute(String username, String routeDescription) {
        triggerEvent(username, "take", routeDescription);
    }

    public void preferRouteType(String userId, String routeType) {
        triggerEvent(userId, "route-preference", routeType);
    }

    public void preferRouteMode(String userId, String routeMode) {
        triggerEvent(userId, "mode-preference", routeMode);
    }

    public void viewRoute(String userId, String routeId) {
        triggerEvent(userId, "view", routeId);
    }

    public void viewRouteLast(String userId, String routeId) {
        triggerEvent(userId, "view-last", routeId);
    }

    public void triggerEvent(String userId, String eventName, String targetValue) {
        System.out.println("User " + userId + eventName + " " + targetValue);
        try {
            eventClient.userActionItem(eventName, userId, targetValue, emptyProperty);
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to trigger event: " + eventName + " to client because of " + e.getMessage());
        }
    }

    public void addUserToClient(String userId) {
        System.out.println("Add user " + userId);
        try {
            eventClient.setUser(userId, emptyProperty);
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to add user " + userId + " to client because of " + e.getMessage());
        }
    }

    public void addRouteToClient(Route route) {
        String routeUniqueId = route.getRouteUniqueId();
        System.out.println("Add route:\n " + routeUniqueId);
        try {
            eventClient.setItem("" + routeUniqueId, route.getRoutePIOProperties());
        } catch (ExecutionException | InterruptedException | IOException e) {
            System.out.println("Failed to add route " + route.getId() + " to client because of " + e.getMessage());
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
