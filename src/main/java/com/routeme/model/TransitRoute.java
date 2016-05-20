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

public class TransitRoute extends Route {

    public TransitRoute(DirectionsRoute googleDirectionsRoute) {
        super(googleDirectionsRoute);
        transportationModes = new ArrayList<String>();
        this.setPredictionIoId();
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
                    departureStop = transitDetails.departureStop;
                }
                arrivalStop = transitDetails.arrivalStop;
                this.predictionIoId += getTransitSummary(transitDetails);
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
        transportationModes.add(transportationMode);
    }

    private String getTransitSummary(TransitDetails transitDetails) {
        TransitLine transitLine = transitDetails.line;
        Vehicle transitVehicle = transitLine.vehicle;
        String headSign = transitDetails.headsign;
        String lineShortName = transitLine.shortName;
        String munichVehicleName = GoogleDirectionsUtility.getMunichTransitVehicleName(transitVehicle);
        addTransportationMode(munichVehicleName);
        return munichVehicleName + lineShortName + "(" + headSign + ")";
    }

}
