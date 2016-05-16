package com.routeme.model;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

public class NonTransitRoute extends Route {

    public NonTransitRoute(DirectionsRoute googleDirectionsRoute) {
        super(googleDirectionsRoute);
        this.setPredictionIoId();
    }

    private void setPredictionIoId() {
        this.predictionIoId = "";
        for (int i = 0; i < steps.length; i++) {
            DirectionsStep currentStep = steps[i];
            this.predictionIoId += getWalkingStepSummary(currentStep.distance, currentStep.duration,
                    currentStep.htmlInstructions);
        }
        this.predictionIoId = "[" + startAddress + "]" + this.predictionIoId;
        this.predictionIoId += "[" + endAddress + "]";
    }

    private String getWalkingStepSummary(Distance distance, Duration duration, String htmlInstructions) {
        String noSpaceBracketedDistance = "(" + distance.humanReadable.replace(" ", "") + ")";
        return htmlInstructions + noSpaceBracketedDistance;
    }

    @Override
    public String toString() {

        return super.toString();
    }
}