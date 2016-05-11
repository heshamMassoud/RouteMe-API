package com.routeme.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public final class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    private User(Factory userFactory) {
        this.name = userFactory.name;
        this.email = userFactory.email;
        this.password = userFactory.password;
    }

    public static Factory getFactory() {
        return new Factory();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void update(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * The Factory's purpose is to shorten the list of parameters for the
     * resource constructor.
     */
    public static class Factory {

        private String name;
        private String email;
        private String password;

        private Factory() {
        }

        public Factory name(String name) {
            this.name = name;
            return this;
        }

        public Factory email(String email) {
            this.email = email;
            return this;
        }

        public Factory password(String password) {
            this.password = email;
            return this;
        }

        public User build() {
            User user = new User(this);
            user.validate(user.getName(), user.getEmail(), user.getPassword());
            return user;
        }
    }

    private void validate(String name, String email, String password) {
        // TODO validate parameters.
    }

}
