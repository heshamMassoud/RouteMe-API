package com.routeme.model;

import java.util.ArrayList;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.StopDetails;
import com.google.maps.model.TransitDetails;
import com.google.maps.model.TransitLine;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Vehicle;
import com.routeme.utility.directions.GoogleDirectionsUtility;
import com.routeme.utility.directions.RouteParseException;

public class TransitRoute extends Route {
    private int numberOfChanges = 0;

    public TransitRoute(DirectionsRoute googleDirectionsRoute) throws RouteParseException {
        super(googleDirectionsRoute);
        if (departureTime == null || arrivalTime == null) {
            throw new RouteParseException();
        }
        transportationModes = new ArrayList<String>();
        this.setPredictionIoId();
    }

    public int getNumberOfChanges() {
        return numberOfChanges;
    }

    private void setPredictionIoId() {
        this.predictionIoId = "";
        StopDetails departureStop = null;
        StopDetails arrivalStop = null;
        for (int i = 0; i < steps.length; i++) {
            DirectionsStep currentStep = steps[i];
            if (currentStep.travelMode == TravelMode.TRANSIT) {
                TransitDetails transitDetails = currentStep.transitDetails;
                if (departureStop == null) {
                    // Capture first station in route
                    departureStop = transitDetails.departureStop;
                    this.startLocationLat = currentStep.startLocation.lat;
                    this.startLocationLng = currentStep.startLocation.lng;
                }
                arrivalStop = transitDetails.arrivalStop;
                this.endLocationLat = currentStep.endLocation.lat;
                this.endLocationLng = currentStep.endLocation.lng;
                this.predictionIoId += getTransitStepSummary(currentStep);
                if (i != steps.length - 1) {
                    this.predictionIoId += "_";
                }
            }
        }
        this.predictionIoId = "[" + departureStop.name + "]" + this.predictionIoId;
        this.predictionIoId += "[" + arrivalStop.name + "]";
        this.predictionIoId = GoogleDirectionsUtility.replaceUmlaut(this.predictionIoId);
    }

    private void addTransportationMode(String transportationMode) {
        this.numberOfChanges++;
        if (!transportationModes.contains(transportationMode)) {
            transportationModes.add(transportationMode);
        }
    }

    private String getTransitStepSummary(DirectionsStep currentStep) {
        TransitDetails transitDetails = currentStep.transitDetails;
        TransitLine transitLine = transitDetails.line;
        Vehicle transitVehicle = transitLine.vehicle;
        String headSign = transitDetails.headsign;
        String lineShortName = transitLine.shortName;
        String munichVehicleName = GoogleDirectionsUtility.getMunichTransitVehicleName(transitVehicle);
        addTransportationMode(munichVehicleName);
        this.stepsData.add(new TransitStep(munichVehicleName, transitDetails.departureStop.name,
                transitDetails.arrivalStop.name, currentStep.duration.humanReadable, transitDetails.departureTime,
                transitDetails.arrivalTime, lineShortName, transitLine.color, headSign));
        return munichVehicleName + lineShortName + "(" + headSign + ")";
    }
}
