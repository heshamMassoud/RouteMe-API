package resources;

import org.springframework.data.annotation.Id;

final class User {
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

    static Factory getFactory() {
        return new Factory();
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

    /**
     * The Factory's purpose is to shorten the list of parameters for the
     * resource constructor.
     */
    static class Factory {

        private String name;
        private String email;
        private String password;

        private Factory() {
        }

        Factory name(String name) {
            this.name = name;
            return this;
        }

        Factory email(String email) {
            this.email = email;
            return this;
        }

        Factory password(String password) {
            this.password = email;
            return this;
        }

        User build() {
            User user = new User(this);
            user.validate(user.getName(), user.getEmail(), user.getPassword());
            return user;
        }
    }

    private void validate(String name, String email, String password) {
        // TODO validate parameters.
    }

}
