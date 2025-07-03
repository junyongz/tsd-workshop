package com.tsd.workshop.telematics.gps.gamtrack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("for local testing only")
public class GamTrackFleetLoginModuleTest {

    @Test
    void getCookies() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        GamTrackFleetLoginModule loginModule = new GamTrackFleetLoginModule(
                props.getProperty("gamtrack.login.username"),
                props.getProperty("gamtrack.login.password"),
                props.getProperty("gamtrack.login.url")
        );

        MultiValueMap<String, ResponseCookie> cookies = loginModule.login().block();
        assertThat(cookies.getFirst("fleetcookpass").getValue()).isEqualTo(props.getProperty("gamtrack.login.password"));

    }

}
