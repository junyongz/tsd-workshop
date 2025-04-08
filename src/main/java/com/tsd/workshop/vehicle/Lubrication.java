package com.tsd.workshop.vehicle;

public enum Lubrication implements Subsystem {
    OIL_PUMP("Circulates engine oil to reduce friction."),
    OIL_FILTER("Removes contaminants from the oil."),
    GREASE_POINTS("Lubricate moving parts like bearings.");

    private final String description;

    Lubrication(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + ": " + description;
    }
}
