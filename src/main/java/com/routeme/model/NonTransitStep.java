package com.routeme.model;

public class NonTransitStep extends Step {
    protected String htmlIntruction;
    protected String distance;

    public NonTransitStep(String transportationMode, String htmlInstruction, String distance) {
        super(transportationMode);
        this.htmlIntruction = htmlInstruction;
        this.distance = distance;
    }

    public String getHtmlIntruction() {
        return htmlIntruction;
    }

    public String getDistance() {
        return distance;
    }

    @Override
    public String getTransportationMode() {
        return super.getTransportationMode();
    }

}
