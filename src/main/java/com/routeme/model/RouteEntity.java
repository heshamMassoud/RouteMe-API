package com.routeme.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "routes")
public final class RouteEntity {
    @Id
    private String id;
    private String pioId;

    public RouteEntity() {
    }

    private RouteEntity(Factory routeFactory) {
        this.pioId = routeFactory.pioId;
    }

    public static Factory getFactory() {
        return new Factory();
    }

    public String getId() {
        return id;
    }

    public String getPioId() {
        return pioId;
    }

    /**
     * The Factory's purpose is to shorten the list of parameters for the
     * resource constructor.
     */
    public static class Factory {

        private String pioId;

        private Factory() {
        }

        public Factory name(String pioId) {
            this.pioId = pioId;
            return this;
        }

        public Factory pioId(String pioId) {
            this.pioId = pioId;
            return this;
        }

        public RouteEntity build() {
            RouteEntity route = new RouteEntity(this);
            return route;
        }
    }

}
