package com.routeme.dto;

import org.hibernate.validator.constraints.NotBlank;

public class RouteDTO {
    @NotBlank
    private String startPoint;

    @NotBlank
    private String endPoint;

    // TODO: Should add more route attributes when known on use case
    // implementation

    public String getEndPoint() {
        return endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
    
    
}
