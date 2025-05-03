package com.tsd.workshop.gps.gussmann;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Properties;

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
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("JNU 1168"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("JPQ 1388"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("JQY 3899"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("JSY 2588"))
                .expectNextMatches(fleetInfo -> fleetInfo.getVehicleNo().equals("JTP 1088"))
                .expectComplete()
                .verify();
    }
}
