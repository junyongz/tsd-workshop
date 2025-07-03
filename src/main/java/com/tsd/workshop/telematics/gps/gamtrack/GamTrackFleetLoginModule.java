package com.tsd.workshop.telematics.gps.gamtrack;

import com.tsd.workshop.PhpSessionIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GamTrackFleetLoginModule {

    private final String username;

    private final transient String password;

    private final String loginUrl;

    public GamTrackFleetLoginModule(@Value("${gamtrack.login.username}") String username,
                                    @Value("${gamtrack.login.password}") String password,
                                    @Value("${gamtrack.login.url}") String loginUrl) {
        this.username = username;
        this.password = password;
        this.loginUrl = loginUrl;
    }

    public Mono<MultiValueMap<String, ResponseCookie>> login() {
        return WebClient.create(loginUrl + "?login=%s&password=%s&remember=99".formatted(username, password))
                .get()
                .cookie("PHPSESSID", PhpSessionIdGenerator.generate())
                .exchangeToMono(resp -> Mono.just(resp.cookies()));
    }
}
