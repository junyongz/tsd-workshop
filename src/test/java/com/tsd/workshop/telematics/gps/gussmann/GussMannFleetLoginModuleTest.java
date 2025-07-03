package com.tsd.workshop.telematics.gps.gussmann;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class GussMannFleetLoginModuleTest {

    @Test
    void login() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        GussMannFleetLoginModule module = new GussMannFleetLoginModule(
                props.getProperty("gussmann.login.username"),
                props.getProperty("gussmann.login.password"),
                props.getProperty("gussmann.login.url")
        );
        String userinfo = module.login().block();
        assertThat(userinfo).isEqualTo("x");
    }
}
