package com.tsd.workshop.maps.render;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Marker {

    private MarkerSize size;

    private String color;

    private String label;

    private String iconUrl;

    private IconAnchor iconAnchor;

    private Coordination[] locations;

    public static class Builder {
        private final Marker marker = new Marker();

        private final List<Coordination> locations = new ArrayList<>();

        public static Builder create() {
            return new Builder();
        }

        public Builder size(MarkerSize size) {
            marker.size = size;
            return this;
        }

        public Builder color(String color) {
            marker.color = color;
            return this;
        }

        public Builder label(String label) {
            marker.label = label;
            return this;
        }

        public Builder icon(String iconUrl) {
            marker.iconUrl = iconUrl;
            return this;
        }

        public Builder iconAnchor(IconAnchor iconAnchor) {
            marker.iconAnchor = iconAnchor;
            return this;
        }

        public Builder addLocation(Coordination location) {
            this.locations.add(location);
            return this;
        }

        public static Marker defaultTruckMarkerWith(Coordination location) {
            return Marker.Builder.create()
                    .label("T")
                    .size(Marker.MarkerSize.MID)
                    .color("black")
                    .addLocation(location).build();
        }

        public Marker build() {
            this.marker.locations = this.locations.toArray(new Coordination[0]);
            return this.marker;
        }

    }

    public enum MarkerSize {
        TINY, MID, SMALL;

        public String value() {
            return name().toLowerCase();
        }
    }

    // IconAnchor can be x,y value too as coordination from label
    public enum IconAnchor {
        TOP, BOTTOM, LEFT, RIGHT, CENTER, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT;

        public String value() {
            return name().toLowerCase();
        }
    }

    public String toUrl() {
        List<String> urlValues = new ArrayList<>();
        // using url
        if (this.iconUrl != null) {
            if (this.iconAnchor != null) {
                urlValues.add("anchor:" + this.iconAnchor.value());
            }
            urlValues.add("icon:"+ URLEncoder.encode(this.iconUrl, StandardCharsets.UTF_8));
        }
        else {
            if (this.size != null) {
                urlValues.add("size:" + this.size.value());
            }
            if (this.color != null) {
                urlValues.add("color:" + this.color);
            }
            if (this.label != null) {
                urlValues.add("label:" + this.label);
            }
        }

        for (Coordination location: this.locations) {
            urlValues.add(location.toUrl());
        }

        return "markers=" + String.join("%7C", urlValues); // %7C for pipe | character
    }
}
