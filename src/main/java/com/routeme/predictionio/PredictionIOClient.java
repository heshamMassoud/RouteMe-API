package com.routeme.predictionio;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.ImmutableMap;

import io.prediction.EventClient;

public class PredictionIOClient {
    private final String accessKey = "aw90ujBL5FynPicxaYnMmAxnTaOXWdCSFD2KtcBsszfHZ4h-j2EP6Wm5woRfZjNX";
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

    public void addUsersToClient(int numberOfUsers) {
        for (int user = 1; user <= numberOfUsers; user++) {
            System.out.println("Add user " + user);
            try {
                eventClient.setUser("" + user, emptyProperty);
            } catch (ExecutionException | InterruptedException | IOException e) {
                System.out.println("Failed to add user " + user + " to client because of " + e.getMessage());
            }
        }
    }

    public void addRoutesToClient(int numberOfRoutes) {
        for (int route = 1; route <= numberOfRoutes; route++) {
            System.out.println("Add item " + route);
            try {
                eventClient.setItem("" + route, emptyProperty);
            } catch (ExecutionException | InterruptedException | IOException e) {
                System.out.println("Failed to add route " + route + " to client because of " + e.getMessage());
            }
        }
    }

    public void randomRouteTakes(int numberOfUsers, int numberOfRoutes) {
        for (int user = 1; user <= numberOfUsers; user++) {
            int routeCounter = 1;
            while (routeCounter <= numberOfRoutes) {
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
