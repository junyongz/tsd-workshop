package com.tsd.workshop.telematics.maps.render;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiParameters {

    private final Location location; // center and zoom

    private final List<Marker> markers = new ArrayList<>();

    private final Size size;

    private Scale scale = Scale.ONE;

    public ApiParameters(Location location, Size size) {
        this.location = location;
        this.size = size;
    }

    public ApiParameters scale(Scale scale) {
        this.scale = scale;
        return this;
    }

    public ApiParameters add(Marker marker) {
        this.markers.add(marker);
        return this;
    }

    String toEncodedUrl() {
        String rawUrl = String.join("&", location.toUrl(), size.toUrl());
        if (scale != null) {
            rawUrl = String.join("&", rawUrl, scale.toUrl());
        }
        if (!markers.isEmpty()) {
            rawUrl = String.join("&", rawUrl, markers.stream().map(Marker::toUrl).collect(Collectors.joining("&")));
        }

        return rawUrl;
    }
}
