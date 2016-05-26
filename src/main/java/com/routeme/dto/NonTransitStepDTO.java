package com.routeme.dto;

public class NonTransitStepDTO extends StepDTO {
    private String htmlInstruction;
    private String distance;

    public NonTransitStepDTO(String transportationMode, String htmlInstruction, String distance) {
        super(transportationMode);
        this.htmlInstruction = htmlInstruction;
        this.distance = distance;
    }

    public String getHtmlInstruction() {
        return htmlInstruction;
    }

    public String getDistance() {
        return distance;
    }

    @Override
    public String getTransportationMode() {
        return super.getTransportationMode();
    }

}
