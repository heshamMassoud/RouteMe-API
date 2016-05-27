package com.routeme.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Util {
    public static class Route {
        // A transit route has only 1 leg (because 0 way points)
        public final static int ONLY_ROUTE_LEG = 0;
        public final static DateFormat TIME_FORMAT = new SimpleDateFormat("KK:mm a");
        public final static String LEAST_TIME_PREFERENCE = "leastTime";
        public final static String LEAST_CHANGES_PREFERENCE = "leastChanges";
    }
    public static class Explanations {
        public final static String LIKED = "LIKED";
        public final static String HYBRID_RECOMMENDER = "HYBRID_RECOMMENDER";
        public final static String POPULARITY = "POPULARITY";
        public final static String LIKES_PREFERENCES = "LIKES_PREFERENCES";
        public final static String PREFERENCES = "PREFERENCES";
    }
}
