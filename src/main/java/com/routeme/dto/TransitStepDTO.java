package com.routeme.dto;

public class TransitStepDTO extends StepDTO {
    protected String startStation;
    protected String endStation;
    protected String duration;
    protected String startTime;
    protected String endTime;
    private String transportationVehicleShortName;
    private String transportationLineColorCode;
    private String transportationLineHeadSign;

    public TransitStepDTO(String transportationMode, String startStation, String endStation, String duration,
            String startTime, String endTime, String transportationVehicleShortName,
            String transportationLineColorCode, String transportationLineHeadSign) {
        super(transportationMode);
        this.startStation = startStation;
        this.endStation = endStation;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getDuration() {
        return duration;
    }

    public String getEndStation() {
        return endStation;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getStartTime() {
        return startTime;
    }
}
