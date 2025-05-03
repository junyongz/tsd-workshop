package com.tsd.workshop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Hooks;

@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Hooks.onOperatorDebug();

        SpringApplication.run(Application.class, args);
    }

    @Bean
    @ConditionalOnProperty(name = "google.maps.integration.enabled", havingValue = "true")
    UrlSigner googleUrlSigner(@Value("${google.platform.api.secret.key}") String base64SecretKey) {
        return new UrlSigner(base64SecretKey, "HmacSHA1");
    }
}