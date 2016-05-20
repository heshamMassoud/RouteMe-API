package com.routeme.dto;

import java.util.ArrayList;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;

public class EventDTO {

    @NotBlank
    private String userId;

    @NotBlank
    private String targetEntityId;

    private Map<String, ArrayList<String>> properites;

    public Map<String, ArrayList<String>> getProperites() {
        return properites;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setProperites(Map<String, ArrayList<String>> properites) {
        this.properites = properites;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
