package com.tsd.workshop.telematics.maps.render;

import com.tsd.workshop.UrlSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.GeneralSecurityException;

@Service
@ConditionalOnProperty(name = "google.maps.integration.enabled", havingValue = "true")
public class GoogleMapsStaticApiClient {
    private final Logger logger = LoggerFactory.getLogger(GoogleMapsStaticApiClient.class);

    @Autowired
    private UrlSigner googleUrlSigner;

    @Autowired
    @Value("${google.maps.static.api.key}")
    private String apiKey;

    @Autowired
    @Value("${google.maps.static.api.base.url}")
    private String baseUrl;

    @Autowired
    @Value("${google.maps.static.api.path.url}")
    private String pathUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Resource> staticImage(ApiParameters params) {
        String urlParams = params.toEncodedUrl() + "&key=" + this.apiKey;

        try {
            logger.trace("encoded url for params is: {}", urlParams);
            String signedUrlPath = googleUrlSigner.sign(this.pathUrl, urlParams);
            String finalUrl = baseUrl.concat(signedUrlPath);

            logger.info("finalUrl: {}", finalUrl);

            WebClient webClient = webClientBuilder.build();

            return webClient.get()
                    .uri(URI.create(finalUrl))
                    .accept(MediaType.IMAGE_PNG)
                    .exchangeToMono(res ->res.bodyToMono(Resource.class));
        }
        catch (GeneralSecurityException ex) {
            throw new IllegalArgumentException("failed to sign url for params: " + urlParams, ex);
        }
    }
}
