package com.routeme.dto;

import java.util.ArrayList;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserDTO {
    private String id;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private ArrayList<String> likedRoutes;

    private ArrayList<String> travelModePreference;

    private ArrayList<String> routeTypePreference;

    public ArrayList<String> getRouteTypePreference() {
        return routeTypePreference;
    }

    public ArrayList<String> getTravelModePreference() {
        return travelModePreference;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getLikedRoutes() {
        return likedRoutes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLikedRoutes(ArrayList<String> likedRoutes) {
        this.likedRoutes = likedRoutes;
    }

    public void setRouteTypePreference(ArrayList<String> routeTypePreference) {
        this.routeTypePreference = routeTypePreference;
    }

    public void setTravelModePreference(ArrayList<String> travelModePreference) {
        this.travelModePreference = travelModePreference;
    }
}
