package com.tsd.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Hooks.onOperatorDebug();

        SpringApplication.run(Application.class, args);
    }
}