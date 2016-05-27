package com.routeme.dto;

public class StepDTO {
    private String transportationMode;

    public StepDTO(String transportationMode) {
        this.transportationMode = transportationMode;
    }

    public String getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
    }

}
