package com.tsd.workshop.telematics.gps.gussmann;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Properties;

@Disabled("for local testing only")
public class GussMannFleetInfoFetcherTest {

    @Test
    void fetch() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        GussMannFleetLoginModule loginModule = new GussMannFleetLoginModule(
                props.getProperty("gussmann.login.username"),
                props.getProperty("gussmann.login.password"),
                props.getProperty("gussmann.login.url")
        );

        GussMannFleetWebClient client = new GussMannFleetWebClient(
                loginModule, props.getProperty("gussmann.fleet.url"));

        GussMannFleetInfoFetcher fetcher = new GussMannFleetInfoFetcher(client);
        StepVerifier.create(fetcher.fetch())
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("XXX XXXX"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("XXX XXXX"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("XXX XXXX"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("XXX XXXX"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("XXX XXXX"))
                .expectComplete()
                .verify();
    }
}
