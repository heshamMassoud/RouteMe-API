package com.routeme.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class SearchRequestDTO {
    @Pattern(regexp = "^[^0-9]*$", message = "Numbers are not allowed")
    @NotBlank
    private String startPoint;

    @Pattern(regexp = "^[^0-9]*$", message = "Numbers are not allowed")
    @NotBlank
    private String endPoint;

    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

}
