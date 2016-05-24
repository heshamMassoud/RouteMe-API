package com.routeme.utility.directions;

public class RouteParseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -3364721094262225836L;

    public RouteParseException() {
        super("Invalid transit route.");
    }
}
