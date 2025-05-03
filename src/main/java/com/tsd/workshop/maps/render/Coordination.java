package com.tsd.workshop.maps.render;

public class Coordination {

    double latitude;
    double longitude;

    Coordination(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordination of(double latitude, double longitude) {
        return new Coordination(latitude, longitude);
    }

    String toUrl() {
        return this.latitude + "," + this.longitude;
    }

    public String asJson() {
        return "{" +
                "\"latitude\": " + latitude +
                ", \"longitude\": " + longitude +
                "}";
    }

    @Override
    public String toString() {
        return "Coordination{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
