package com.tsd.workshop.gps.gamtrack;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class GamTrackFleetWebClientTest {

    @Test
    void getHtmlContent() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        GamTrackFleetLoginModule loginModule = new GamTrackFleetLoginModule(
                props.getProperty("gamtrack.login.username"),
                props.getProperty("gamtrack.login.password"),
                props.getProperty("gamtrack.login.url")
        );

        GamTrackFleetWebClient client = new GamTrackFleetWebClient(
                loginModule, props.getProperty("gamtrack.fleet.url"));
        Document doc = client.fleetInfo().block();
        List<String> odometers = doc.selectStream("tr")
                .map(elem -> elem.select("td:nth-child(11)"))
                .map(Elements::text)
                .toList();

        assertThat(odometers).isNull();
    }
}
