package com.routeme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.routeme.utility.directions.GoogleDirectionsUtility;

public class NonTransitRoute extends Route {

    public NonTransitRoute(DirectionsRoute googleDirectionsRoute) {
        super(googleDirectionsRoute);
        this.setPredictionIoId();
        this.setTransportationModes();
    }

    private void setPredictionIoId() {
        this.predictionIoId = "";
        for (int i = 0; i < steps.length; i++) {
            DirectionsStep currentStep = steps[i];
            String transportationMode = currentStep.travelMode.name();
            String htmlInstruction = currentStep.htmlInstructions;
            String distance = currentStep.distance.humanReadable;
            
            this.predictionIoId += getStepSummary(htmlInstruction, distance);
            addStepData(transportationMode, htmlInstruction, distance);
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
    
    private void addStepData(String transportationMode, String htmlInstruction, String distance) {
        Map<String, String> stepData = new HashMap<String, String>();
        stepData.put(TRANSPORTATION_MODE_KEY, transportationMode);
        stepData.put(NON_TRANSIT_STEP_HTML_INSTRUCTION, htmlInstruction);
        stepData.put(NON_TRANSIT_STEP_DISTANCE, distance);
        this.stepsData.add(stepData);
    }
}
