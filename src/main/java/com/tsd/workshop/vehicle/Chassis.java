package com.tsd.workshop.vehicle;

public enum Chassis implements Subsystem {
    FRAME("Structural backbone supporting all components."),
    SUSPENSION_SYSTEM("Absorbs shocks and maintains stability."),
    WHEELS_AND_TIRES("Provide traction and support the load."),
    FIFTH_WHEEL_COUPLING("Connects the tractor to the trailer.");

    private final String description;

    Chassis(String description) {
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
