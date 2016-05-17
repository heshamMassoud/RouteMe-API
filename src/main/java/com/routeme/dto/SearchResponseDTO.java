package com.routeme.dto;

import java.util.List;

public class SearchResponseDTO {
    private List<RouteDTO> routeResults;

    public List<RouteDTO> getRouteResults() {
        return routeResults;
    }

    public void setRouteResults(List<RouteDTO> routeResults) {
        this.routeResults = routeResults;
    }
}
