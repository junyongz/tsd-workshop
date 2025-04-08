package com.tsd.workshop.vehicle;

public enum Powertrain implements Subsystem {
    ENGINE("Primary power source, usually a diesel engine."),
    TRANSMISSION("Transfers power from the engine to the drivetrain."),
    DRIVESHAFT("Transmits torque from the transmission to the differential."),
    DIFFERENTIAL("Distributes power to the wheels."),
    AXLES("Support the weight and transfer power to the wheels.");

    private final String description;

    Powertrain(String description) {
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
