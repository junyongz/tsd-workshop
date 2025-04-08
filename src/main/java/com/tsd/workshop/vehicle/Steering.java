package com.tsd.workshop.vehicle;

public enum Steering implements Subsystem {
    STEERING_WHEEL("Driver input device for steering."),
    STEERING_GEARBOX("Converts rotational input into movement."),
    POWER_STEERING_SYSTEM("Assists in turning the wheels."),
    TIE_RODS_AND_LINKAGES("Connect steering system to the wheels.");

    private final String description;

    Steering(String description) {
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
