package com.tsd.workshop;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlSignerTest {

    @Test
    void signDefault() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        String apiKey = props.getProperty("google.maps.static.api.key");
        UrlSigner urlSigner = new UrlSigner(props.getProperty("google.platform.api.secret.key"), "HmacSHA1");

        assertThat(urlSigner.sign("/maps/api/staticmap?center=Z%C3%BCrich&zoom=12&size=400x400&key="+apiKey))
                .isEqualTo("/maps/api/staticmap?center=Z%C3%BCrich&zoom=12&size=400x400&key="+apiKey+"&signature=-ZR-9pym2LAVulF9JGWxAn9wgMs=");
    }

    @Test
    void signSpecial() throws Exception {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        String apiKey = props.getProperty("google.maps.static.api.key");
        UrlSigner urlSigner = new UrlSigner(props.getProperty("google.platform.api.secret.key"), "HmacSHA1");

        assertThat(urlSigner.sign("/maps/api/staticmap?center=1.6092034,103.6738986&zoom=14&size=400x400&scale=2&markers=anchor:top%7Cicon:https%3A%2F%2Fimg.icons8.com%2F%3Fsize%3D40%26id%3DhwFnqExjOFhx%26format%3Dpng%26color%3D000000%7C1.6093860354422986,103.67398220433418&key="+ apiKey))
                .isEqualTo("/maps/api/staticmap?center=Z%C3%BCrich&zoom=12&size=400x400&key="+apiKey+"&signature=h1BxK3r7DOje08S9eMyjNFBH8as=");


    }
}
