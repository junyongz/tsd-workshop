package com.tsd.workshop.telematics.maps.render;

import com.tsd.workshop.UrlSigner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.assertj.core.api.Fail.fail;

@Disabled
@DisplayName("dont do this too much because having actual API call, disable it after run")
public class GoogleMapsStaticApiClientTest {

    private GoogleMapsStaticApiClient client() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        String apiKey = props.getProperty("google.maps.static.api.key");
        String baseUrl = props.getProperty("google.maps.static.api.base.url");
        String pathUrl = props.getProperty("google.maps.static.api.path.url");

        UrlSigner urlSigner = new UrlSigner(props.getProperty("google.platform.api.secret.key"), "HmacSHA1");

        GoogleMapsStaticApiClient client = new GoogleMapsStaticApiClient();
        ReflectionTestUtils.setField(client, "urlSigner", urlSigner);
        ReflectionTestUtils.setField(client, "apiKey", apiKey);
        ReflectionTestUtils.setField(client, "baseUrl", baseUrl);
        ReflectionTestUtils.setField(client, "pathUrl", pathUrl);

        return client;
    }

    @Test
    void samplingPlaceNameKualaLumpur() throws Exception {
        Mono<Resource> res = client().staticImage(
                new ApiParameters(
                    Location.of("Kuala Lumpur", "10"), Size.of(400,400)
                ).scale(Scale.TWO)
        );

        StepVerifier.create(res)
                .assertNext(resource -> {
                    try {
                        Files.write(Path.of("hello.png"), resource.getContentAsByteArray());
                    } catch (IOException e) {
                        fail("failed to write to 'hello.png'" + e);
                    }
                })
                .expectComplete()
                .verify();
    }

    @Test
    void samplingPlaceLatAndLongWithMarker() throws Exception {
        Mono<Resource> res = client().staticImage(
                new ApiParameters(
                        Location.of(Coordination.of(1.6092034,103.6738986), "14"), Size.of(400,400)
                ).scale(Scale.TWO)
                        .add(Marker.Builder.create()
                        .color("red")
                        .size(Marker.MarkerSize.MID)
                        .label("T") // only MID can show the label
                        .addLocation(Coordination.of(1.6093860354422986, 103.67398220433418))
                        .build())
        );

        StepVerifier.create(res)
                .assertNext(resource -> {
                    try {
                        Files.write(Path.of("hello.png"), resource.getContentAsByteArray());
                    } catch (IOException e) {
                        fail("failed to write to 'hello.png'" + e);
                    }
                })
                .expectComplete()
                .verify();
    }

    @Test
    void samplingPlaceLatAndLongWithIconMarker() throws Exception {
        Mono<Resource> res = client().staticImage(
                new ApiParameters(
                        Location.of(Coordination.of(1.6092034,103.6738986), "14"), Size.of(400,400)
                ).scale(Scale.TWO)
                        .add(Marker.Builder.create()
                                .icon("https://img.icons8.com/?size=100&id=8jWj9cJkofhf&format=png&color=000000")
                                .iconAnchor(Marker.IconAnchor.TOP)
                                .addLocation(Coordination.of(1.6093860354422986, 103.67398220433418))
                                .build())
        );

        StepVerifier.create(res)
                .assertNext(resource -> {
                    try {
                        Files.write(Path.of("hello.png"), resource.getContentAsByteArray());
                    } catch (IOException e) {
                        fail("failed to write to 'hello.png'" + e);
                    }
                })
                .expectComplete()
                .verify();
    }
}
