package com.routeme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Route {
    private String id;
    private String routeUniqueId;
    private String startPoint;
    private String endPoint;
    private ArrayList<String> tranportationModes;// TODO MAKE IT LIST OF ENUMS
    private String routeType; // TODO MAKE IT LIST OF ENUMS

    public Route() {
    }

    private Route(Factory routeFactory) {
        this.startPoint = routeFactory.startPoint;
        this.endPoint = routeFactory.endPoint;
        this.routeType = routeFactory.routeType;
        this.tranportationModes = routeFactory.transportations;
    }

    public static Factory getFactory() {
        return new Factory();
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getRouteType() {
        return routeType;
    }

    public ArrayList<String> getTransportations() {
        return tranportationModes;
    }

    public String getId() {
        return id;
    }

    public String getRouteUniqueId() {
        // TODO: Should set it once on initialization!
        routeUniqueId = startPoint;
        for (int i = 0; i < tranportationModes.size(); i++) {
            routeUniqueId += "_" + tranportationModes.get(i);
        }
        routeUniqueId += "_" + endPoint;
        return routeUniqueId;
    }

    public Map<String, Object> getRoutePIOProperties() {
        // TODO Should not use the getRouteUniquerId method below. But rather an
        // instance variable.
        Map<String, Object> properties = new HashMap<String, Object>();
        ArrayList<String> routeId = new ArrayList<String>();
        ArrayList<String> routeType = new ArrayList<String>();
        routeId.add(getRouteUniqueId());
        routeType.add(this.routeType);
        properties.put("routeId", routeId);
        properties.put("transportationModes", tranportationModes);
        properties.put("routeType", routeType);
        return properties;
    }

    /**
     * The Factory's purpose is to shorten the list of parameters for the
     * resource constructor.
     */
    public static class Factory {

        private String startPoint;
        private String endPoint;
        private String routeType;
        private ArrayList<String> transportations;

        private Factory() {
        }

        public Factory startPoint(String startPoint) {
            this.startPoint = startPoint;
            return this;
        }

        public Factory endPoint(String endPoint) {
            this.endPoint = endPoint;
            return this;
        }

        public Factory routeType(String routeType) {
            this.routeType = routeType;
            return this;
        }

        public Factory transportations(ArrayList<String> transportations) {
            this.transportations = transportations;
            return this;
        }

        public Route build() {
            Route route = new Route(this);
            return route;
        }
    }
}
