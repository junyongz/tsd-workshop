package com.tsd.workshop.maps.render;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Location {

    private Coordination center;

    private String centerPlace;

    private String zoom;

    public static Location of(Coordination center, String zoom) {
        Location loc = new Location();
        loc.center = center;
        loc.zoom = zoom;
        return loc;
    }

    public static Location of(String centerPlace, String zoom) {
        Location loc = new Location();
        loc.centerPlace = centerPlace;
        loc.zoom = zoom;
        return loc;
    }

    String toUrl() {
        return "center=%s&zoom=%s".formatted(
                (centerPlace == null ? center.latitude + "," + center.longitude : URLEncoder.encode(centerPlace, StandardCharsets.UTF_8)),
                Zoom.of(zoom));
    }
}
