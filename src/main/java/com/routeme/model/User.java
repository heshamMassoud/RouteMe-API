package com.routeme.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public final class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    @DBRef
    private List<RouteEntity> likedRoutes;

    public User() {
    }

    private User(Factory userFactory) {
        this.username = userFactory.username;
        this.email = userFactory.email;
        this.password = userFactory.password;
        this.likedRoutes = new ArrayList<RouteEntity>();
    }

    public static Factory getFactory() {
        return new Factory();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<RouteEntity> getRoutesLiked() {
        return likedRoutes;
    }

    public void addlikedRoute(RouteEntity route) {
        likedRoutes.add(route);
    }

    public void update(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * The Factory's purpose is to shorten the list of parameters for the
     * resource constructor.
     */
    public static class Factory {

        private String username;
        private String email;
        private String password;

        private Factory() {
        }

        public Factory name(String username) {
            this.username = username;
            return this;
        }

        public Factory email(String email) {
            this.email = email;
            return this;
        }

        public Factory password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            User user = new User(this);
            return user;
        }
    }

}
