package com.routeme.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteDTO {
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String distance;
    private String startAddress;
    private String endAddress;
    private String overviewPolyLine;
    private String routeSummary;
    private String predictionIoId;
    private ArrayList<String> transportationModes;

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public String getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public String getPredictionIoId() {
        return predictionIoId;
    }

    public String getRouteSummary() {
        return routeSummary;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public void setOverviewPolyLine(String overviewPolyLine) {
        this.overviewPolyLine = overviewPolyLine;
    }

    public void setPredictionIoId(String predictionIoId) {
        this.predictionIoId = predictionIoId;
    }

    public void setRouteSummary(String routeSummary) {
        this.routeSummary = routeSummary;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public ArrayList<String> getTransportationModes() {
        return transportationModes;
    }

    public void setTransportationModes(ArrayList<String> transportationModes) {
        this.transportationModes = transportationModes;
    }

    public Map<String, Object> getRoutePIOProperties(String routeType) {
        Map<String, Object> properties = new HashMap<String, Object>();
        ArrayList<String> routeIdArray = new ArrayList<String>();
        ArrayList<String> routeTypeArray = new ArrayList<String>();
        routeIdArray.add(getPredictionIoId());
        routeTypeArray.add(routeType);
        properties.put("routeId", routeIdArray);
        properties.put("transportationModes", getTransportationModes());
        properties.put("routeType", routeTypeArray);
        return properties;
    }

}
