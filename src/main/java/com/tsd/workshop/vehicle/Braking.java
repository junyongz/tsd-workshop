package com.tsd.workshop.vehicle;

public enum Braking implements Subsystem {
    AIR_BRAKE_SYSTEM("Uses compressed air to activate brakes."),
    SERVICE_BRAKES("Primary braking mechanism for stopping."),
    PARKING_BRAKES("Secures the truck when stationary."),
    ANTI_LOCK_BRAKING_SYSTEM("Prevents wheel lockup during sudden stops."),
    TRAILER_BRAKE_CONTROLLER("Manages trailer braking in sync with the tractor.");

    private final String description;

    Braking(String description) {
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
