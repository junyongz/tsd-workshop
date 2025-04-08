package com.tsd.workshop.vehicle;

public enum Trailer implements Subsystem {
    LANDING_GEAR("Supports the trailer when detached."),
    CARGO_SECURING_SYSTEM("Includes tie-downs and straps for freight."),
    REFRIGERATION_UNIT("Maintains temperature for perishable goods."),
    DOORS_AND_SEALS("Ensure secure and weatherproof cargo storage.");

    private final String description;

    Trailer(String description) {
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
