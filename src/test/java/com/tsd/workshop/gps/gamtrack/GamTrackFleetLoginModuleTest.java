package com.tsd.workshop.gps.gamtrack;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(cookies.getFirst("fleetcookpass").getValue()).isEqualTo(props.getProperty("gumtrack.login.password"));

    }

}
