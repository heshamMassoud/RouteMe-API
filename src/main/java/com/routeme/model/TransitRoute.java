package com.routeme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public TransitRoute(DirectionsRoute googleDirectionsRoute) throws RouteParseException {
        super(googleDirectionsRoute);
        if (departureTime == null || arrivalTime == null) {
            throw new RouteParseException();
        }
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
                this.predictionIoId += getTransitStepSummary(transitDetails);
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

    private String getTransitStepSummary(TransitDetails transitDetails) {
        TransitLine transitLine = transitDetails.line;
        Vehicle transitVehicle = transitLine.vehicle;
        String headSign = transitDetails.headsign;
        String lineShortName = transitLine.shortName;
        String munichVehicleName = GoogleDirectionsUtility.getMunichTransitVehicleName(transitVehicle);
        addTransportationMode(munichVehicleName);
        setStepData(munichVehicleName, lineShortName, transitLine.color, headSign);
        return munichVehicleName + lineShortName + "(" + headSign + ")";
    }

    private void setStepData(String vehicleName, String lineShortName, String lineHexColor, String headSign) {
        Map<String, String> stepData = new HashMap<String, String>();
        stepData.put(TRANSPORTATION_MODE_KEY, vehicleName);
        stepData.put(TRANSIT_VEHICLE_SHORT_NAME_KEY, lineShortName);
        stepData.put(TRANSIT_LINE_HEX_COLOR, lineHexColor);
        stepData.put(TRANSIT_LINE_HEADSIGN, headSign);
        this.stepsData.add(stepData);
    }
}
