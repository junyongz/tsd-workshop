package com.tsd.workshop.telematics.maps.render;

public class Zoom {

    static String[] values = {
            "0",   "1",  "2",  "3",  "4",  "5",  "6",  "7",  "8",  "9",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "21+"
    };

    static String of(String val) {
        boolean found = false;
        for (String value : values) {
            if (value.equals(val)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("not a valid zoom value: " + val);
        }

        return val;
    }

}
