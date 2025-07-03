package com.tsd.workshop.telematics.gps.gamtrack;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Properties;

@Disabled("only for local testing")
public class GamTrackFleetInfoFetcherTest {

    @Test
    void fetch() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        GamTrackFleetLoginModule loginModule = new GamTrackFleetLoginModule(
                props.getProperty("gamtrack.login.username"),
                props.getProperty("gamtrack.login.password"),
                props.getProperty("gamtrack.login.url")
        );

        GamTrackFleetWebClient client = new GamTrackFleetWebClient(
                loginModule, props.getProperty("gamtrack.fleet.url"));

        GamTrackFleetInfoFetcher fetcher = new GamTrackFleetInfoFetcher(client);
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
