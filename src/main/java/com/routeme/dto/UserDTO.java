package com.routeme.dto;

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

    @NotBlank
    private String confirmationPassword;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
