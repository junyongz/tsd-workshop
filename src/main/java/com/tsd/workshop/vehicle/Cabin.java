package com.tsd.workshop.vehicle;

public enum Cabin implements Subsystem {
    DRIVERS_SEAT("Adjustable seat for driver comfort."),
    DASHBOARD("Displays speed, fuel level, and engine status."),
    HVAC_SYSTEM("Provides heating, ventilation, and air conditioning."),
    SLEEPER_COMPARTMENT("Rest area for long-haul drivers."),
    WINDOWS_AND_MIRRORS("Ensure visibility around the vehicle.");

    private final String description;

    Cabin(String description) {
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
