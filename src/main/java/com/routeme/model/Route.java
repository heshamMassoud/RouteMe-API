package com.routeme.model;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;

public class Route {
    protected double startLocationLat;
    protected double startLocationLng;
    protected double endLocationLat;
    protected double endLocationLng;
    protected DateTime departureTime;
    protected DateTime arrivalTime;
    protected Duration duration;
    protected Distance distance;
    protected DirectionsStep[] steps;
    protected String startAddress;
    protected String endAddress;
    protected EncodedPolyline overviewPolyLine;
    protected String routeSummary;
    protected ArrayList<String> transportationModes;
    protected ArrayList<Step> stepsData = new ArrayList<Step>();
    protected String predictionIoId;

    public Route(DirectionsRoute googleDirectionsRoute) {
        DirectionsLeg routeLeg = googleDirectionsRoute.legs[Util.Route.ONLY_ROUTE_LEG];
        this.departureTime = routeLeg.departureTime;
        this.arrivalTime = routeLeg.arrivalTime;
        this.duration = routeLeg.duration;
        this.distance = routeLeg.distance;
        this.steps = routeLeg.steps;
        this.startAddress = routeLeg.startAddress.trim();
        this.endAddress = routeLeg.endAddress.trim();
        this.overviewPolyLine = googleDirectionsRoute.overviewPolyline;
        this.routeSummary = googleDirectionsRoute.summary;
    }

    public String getPredictionIoId() {
        return predictionIoId;
    }

    public DateTime getArrivalTime() {
        return arrivalTime;
    }

    public DateTime getDepartureTime() {
        return departureTime;
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public EncodedPolyline getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public String getRouteSummary() {
        return routeSummary;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public DirectionsStep[] getSteps() {
        return steps;
    }

    public ArrayList<String> getTransportationModes() {
        return transportationModes;
    }

    public ArrayList<Step> getStepsData() {
        return stepsData;
    }

    public double getEndLocationLat() {
        return endLocationLat;
    }

    public double getEndLocationLng() {
        return endLocationLng;
    }

    public double getStartLocationLat() {
        return startLocationLat;
    }

    public double getStartLocationLng() {
        return startLocationLng;
    }
}
