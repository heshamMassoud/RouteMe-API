package com.routeme;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

//Configuration for "live" connections
@Configuration
public class MongoConfig {

    @Value("25792")
    private String mongoPort;

    @Value("routeme")
    private String mongoDatabase;

    @Bean(name = "mongoClient")
    public MongoClient mongoClient() throws IOException {
        MongoClientURI mongoClientURI = new MongoClientURI("mongodb://dev:hesham@ds025792.mlab.com:" + mongoPort + "/"
                + mongoDatabase);
        return new MongoClient(mongoClientURI);
    }

    @Autowired
    @Bean(name = "mongoDbFactory")
    public MongoDbFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoDbFactory(mongoClient, mongoDatabase);
    }

    @Autowired
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, mongoDatabase);
    }
}