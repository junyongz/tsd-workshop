package com.tsd.workshop.gps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class GussMannFleetWebClientTest {

    @Test
    void fleetInfo() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        GussMannFleetLoginModule loginModule = new GussMannFleetLoginModule(
                props.getProperty("gussmann.login.username"),
                props.getProperty("gussmann.login.password"),
                props.getProperty("gussmann.login.url")
        );

        GussMannFleetWebClient client = new GussMannFleetWebClient(loginModule,props.getProperty("gussmann.fleet.url"));

        JsonNode bigJson = client.fleetInfo().block();
        Map<String, Long> odometerByVehicle = StreamSupport.stream(bigJson.get("aaData").spliterator(), false)
                        .collect(Collectors.toMap(jn -> jn.get(2).asText(), jn->jn.get(10).asLong()));

        assertThat(odometerByVehicle).isNull();
    }
}
