package com.fiap.tech_challenge.parte1.ms_users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Users microservice application.
 * <p>
 * This class bootstraps the Spring Boot application context and starts the embedded server.
 * </p>
 */
@SpringBootApplication
public class MsUsersApplication {

    /**
     * Application main method used to run the Spring Boot application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(MsUsersApplication.class, args);
    }
}
