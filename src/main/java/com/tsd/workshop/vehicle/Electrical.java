package com.tsd.workshop.vehicle;

public enum Electrical implements Subsystem {
    BATTERY("Powers electrical components and starts the engine."),
    ALTERNATOR("Charges the battery and supplies power while running."),
    WIRING_HARNESS("Distributes electricity to components."),
    LIGHTING_SYSTEM("Includes headlights, taillights, and signals."),
    ELECTRONIC_CONTROL_UNIT("Manages engine performance and diagnostics.");

    private final String description;

    Electrical(String description) {
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
