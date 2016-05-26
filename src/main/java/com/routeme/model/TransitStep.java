package com.routeme.model;

public class TransitStep extends Step {

    protected String transportationVehicleShortName;
    protected String transportationLineColorCode;
    protected String transportationLineHeadSign;

    public TransitStep(String transportationMode, String transportationVehicleShortName,
            String transportationLineColorCode, String transportationLineHeadSign) {
        super(transportationMode);
        this.transportationVehicleShortName = transportationVehicleShortName;
        this.transportationLineColorCode = transportationLineColorCode;
        this.transportationLineHeadSign = transportationLineHeadSign;
    }

    @Override
    public String getTransportationMode() {
        return super.getTransportationMode();
    }

    public String getTransportationLineColorCode() {
        return transportationLineColorCode;
    }

    public String getTransportationLineHeadSign() {
        return transportationLineHeadSign;
    }

    public String getTransportationVehicleShortName() {
        return transportationVehicleShortName;
    }

}
