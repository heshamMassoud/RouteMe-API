package com.routeme;

import io.prediction.EngineClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class AppConfig {
    public static EngineClient engineClient;

    public static void main(String[] args) {
        engineClient = new EngineClient("http://localhost:8000");
        SpringApplication.run(AppConfig.class, args);
    }
}