package com.routeme.model;

import org.joda.time.DateTime;

public class TransitStep extends Step {

    protected String startStation;
    protected String endStation;
    protected String duration;
    protected DateTime startTime;
    protected DateTime endTime;
    protected String transportationVehicleShortName;
    protected String transportationLineColorCode;
    protected String transportationLineHeadSign;

    public TransitStep(String transportationMode, String startStation, String endStation, String duration,
            DateTime startTime, DateTime endTime, String transportationVehicleShortName,
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

    public String getEndStation() {
        return endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getDuration() {
        return duration;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public DateTime getStartTime() {
        return startTime;
    }

}
