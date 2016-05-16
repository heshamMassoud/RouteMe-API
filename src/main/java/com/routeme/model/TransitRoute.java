package com.routeme.model;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.StopDetails;
import com.google.maps.model.TransitDetails;
import com.google.maps.model.TransitLine;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Vehicle;
import com.routeme.utility.directions.GoogleDirectionsUtility;

public class TransitRoute extends Route {

    public TransitRoute(DirectionsRoute googleDirectionsRoute) {
        super(googleDirectionsRoute);
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
    }

    private String getTransitSummary(TransitDetails transitDetails) {
        TransitLine transitLine = transitDetails.line;
        Vehicle transitVehicle = transitLine.vehicle;
        String headSign = transitDetails.headsign;
        String lineShortName = transitLine.shortName;
        return GoogleDirectionsUtility.getMunichTransitVehicleName(transitVehicle) + lineShortName + "(" + headSign
                + ")";
    }

    private String getWalkingStepsSummary(DirectionsStep[] walkingSteps) {
        String walkingStepSummary = "";
        for (int i = 0; i < walkingSteps.length; i++) {
            DirectionsStep walkingStep = walkingSteps[i];
            DirectionsStep[] innerWalkingSteps = walkingStep.steps;
            if (innerWalkingSteps != null) {
                walkingStepSummary += getInnerWalkingStepsSummary(innerWalkingSteps);
            } else {
                walkingStepSummary += getWalkingStepSummary(walkingStep.distance, walkingStep.duration,
                        walkingStep.htmlInstructions);
            }
            if (i != walkingSteps.length - 1) {
                walkingStepSummary += "_";
            }

        }
        return walkingStepSummary;
    }

    private String getInnerWalkingStepsSummary(DirectionsStep[] innerWalkingSteps) {
        String innerWalkingStepsSummary = "";
        for (int j = 0; j < innerWalkingSteps.length; j++) {
            DirectionsStep innerwalkingStep = innerWalkingSteps[j];
            innerWalkingStepsSummary += getWalkingStepSummary(innerwalkingStep.distance, innerwalkingStep.duration,
                    innerwalkingStep.htmlInstructions);
            if (j != innerWalkingSteps.length - 1) {
                innerWalkingStepsSummary += "_";
            }
        }
        return innerWalkingStepsSummary;
    }

    private String getWalkingStepSummary(Distance distance, Duration duration, String htmlInstructions) {
        return htmlInstructions + "(" + distance.humanReadable + "_" + duration.humanReadable + ")";
    }

}
