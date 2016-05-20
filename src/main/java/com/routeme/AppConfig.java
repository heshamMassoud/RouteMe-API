package com.routeme;

import io.prediction.EngineClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppConfig {
    public static EngineClient engineClient;

    public static void main(String[] args) {
        engineClient = new EngineClient("http://localhost:8000");
        SpringApplication.run(AppConfig.class, args);
    }
}