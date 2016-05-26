package com.routeme.dto;

public class TransitStepDTO extends StepDTO {
    private String transportationVehicleShortName;
    private String transportationLineColorCode;
    private String transportationLineHeadSign;

    public TransitStepDTO(String transportationMode, String transportationVehicleShortName,
            String transportationLineColorCode, String transportationLineHeadSign) {
        super(transportationMode);
        this.transportationVehicleShortName = transportationVehicleShortName;
        this.transportationLineColorCode = transportationLineColorCode;
        this.transportationLineHeadSign = transportationLineHeadSign;
    }

    public String getTransportationVehicleShortName() {
        return transportationVehicleShortName;
    }

    public String getTransportationLineColorCode() {
        return transportationLineColorCode;
    }

    public String getTransportationLineHeadSign() {
        return transportationLineHeadSign;
    }

    @Override
    public String getTransportationMode() {
        return super.getTransportationMode();
    }
}
