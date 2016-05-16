package com.routeme.dto;

import org.hibernate.validator.constraints.NotBlank;

public class SearchRequestDTO {
    @NotBlank
    private String startPoint;

    @NotBlank
    private String endPoint;

    public String getEndPoint() {
        return endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

}
