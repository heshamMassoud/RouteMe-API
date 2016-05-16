package com.routeme.model;

import org.joda.time.DateTime;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;

public class Route {
    protected DateTime departureTime;
    protected DateTime arrivalTime;
    protected Duration duration;
    protected Distance distance;
    protected DirectionsStep[] steps;
    protected String startAddress;
    protected String endAddress;
    protected EncodedPolyline overviewPolyLine;
    protected String routeSummary;
    // A transit route has only 1 leg (because 0 way points)
    protected final int LEG_INDEX = 0;
    protected String predictionIoId;

    public Route(DirectionsRoute googleDirectionsRoute) {
        DirectionsLeg routeLeg = googleDirectionsRoute.legs[LEG_INDEX];
        this.departureTime = routeLeg.departureTime;
        this.arrivalTime = routeLeg.arrivalTime;
        this.duration = routeLeg.duration;
        this.distance = routeLeg.distance;
        this.steps = routeLeg.steps;
        this.startAddress = routeLeg.startAddress.trim();
        this.endAddress = routeLeg.endAddress.trim();
        this.overviewPolyLine = googleDirectionsRoute.overviewPolyline;
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
}
