package com.tsd.workshop.gps.gamtrack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GamTrackFleetWebClient {

    private final GamTrackFleetLoginModule loginModule;

    private final String fleetUrl;

    public GamTrackFleetWebClient(GamTrackFleetLoginModule loginModule, @Value("${gamtrack.fleet.url}") String fleetUrl) {
        this.loginModule = loginModule;
        this.fleetUrl = fleetUrl;
    }

    public Mono<Document> fleetInfo() {
        return loginModule.login().flatMap(
                cookies ->
                WebClient.create(fleetUrl)
                        .get()
                        .cookies(cookiesConsumer -> {
                            cookies.asSingleValueMap().forEach((k, cookie) -> {
                                cookiesConsumer.add(k, cookie.getValue());
                            });
                        })
                        .exchangeToMono(res -> res.bodyToMono(String.class))
                        .map(Jsoup::parse));
    }
}
