package com.routeme.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Util {
    public static class Route {
        // A transit route has only 1 leg (because 0 way points)
        public static int ONLY_ROUTE_LEG = 0;
        public static DateFormat TIME_FORMAT = new SimpleDateFormat("KK:mm a");
    }
}
