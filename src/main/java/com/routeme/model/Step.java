package com.routeme.model;

public class Step {
    protected String transportationMode;

    public Step(String transportationMode) {
        this.transportationMode = transportationMode;
    }

    public String getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
    }
}
