package com.tsd.workshop.telematics.gps.gussmann;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Guss Mann, first to retrieve the credential info from cookies.
 * <p>
 *     Then using cookies putting credential could call their various APIs
 * </p>
 */
@Component
public class GussMannFleetWebClient {

    private final GussMannFleetLoginModule loginModule;

    private final String fleetUrl;

    public GussMannFleetWebClient(GussMannFleetLoginModule loginModule,
                                  @Value("${gussmann.fleet.url}") String fleetUrl) {
        this.loginModule = loginModule;
        this.fleetUrl = fleetUrl;
    }

    public Mono<JsonNode> fleetInfo() {
        return loginModule.login().flatMap(
                userinfo -> WebClient.create(fleetUrl)
                        .get()
                        .cookie("userinfo", userinfo)
                        .exchangeToMono(res -> res.bodyToMono(byte[].class))
                        .map(body -> {
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                return objectMapper.readValue(body, JsonNode.class);
                            } catch (IOException ex) {
                                throw new IllegalArgumentException("Failed to parse JSON: " + ex.getMessage(), ex);
                            }
                        }));
    }
}
