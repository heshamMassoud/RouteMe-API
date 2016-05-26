package com.routeme.model;

import java.util.ArrayList;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.routeme.utility.directions.GoogleDirectionsUtility;

public class NonTransitRoute extends Route {

    public NonTransitRoute(DirectionsRoute googleDirectionsRoute) {
        super(googleDirectionsRoute);
        this.setPredictionIoId();
        this.setTransportationModes();
        this.startLocationLat = googleDirectionsRoute.legs[Util.Route.ONLY_ROUTE_LEG].startLocation.lat;
        this.startLocationLng = googleDirectionsRoute.legs[Util.Route.ONLY_ROUTE_LEG].startLocation.lng;
        this.endLocationLat = googleDirectionsRoute.legs[Util.Route.ONLY_ROUTE_LEG].endLocation.lat;
        this.endLocationLng = googleDirectionsRoute.legs[Util.Route.ONLY_ROUTE_LEG].endLocation.lng;
    }

    private void setPredictionIoId() {
        this.predictionIoId = "";
        for (int i = 0; i < steps.length; i++) {
            DirectionsStep currentStep = steps[i];
            String transportationMode = currentStep.travelMode.name();
            String htmlInstruction = currentStep.htmlInstructions;
            String distance = currentStep.distance.humanReadable;

            this.predictionIoId += getStepSummary(htmlInstruction, distance);
            this.stepsData.add(new NonTransitStep(transportationMode, htmlInstruction, distance));
        }
        this.predictionIoId = "[" + startAddress + "]" + this.predictionIoId;
        this.predictionIoId += "[" + endAddress + "]";
        this.predictionIoId = GoogleDirectionsUtility.replaceUmlaut(this.predictionIoId);
    }

    private String getStepSummary(String htmlInstruction, String distance) {
        String noSpaceBracketedDistance = "(" + distance.replace(" ", "") + ")";
        return htmlInstruction + noSpaceBracketedDistance;
    }

    private void setTransportationModes() {
        final int firstStepIndex = 0;
        transportationModes = new ArrayList<String>();
        transportationModes.add(steps[firstStepIndex].travelMode.name());
    }
}
