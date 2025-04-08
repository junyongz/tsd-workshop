package com.tsd.workshop.vehicle;

public enum Fuel implements Subsystem {
    FUEL_TANK("Stores diesel or alternative fuel."),
    FUEL_PUMP("Delivers fuel from the tank to the engine."),
    FUEL_INJECTORS("Spray fuel into the combustion chambers."),
    FUEL_LINES_AND_FILTERS("Transport fuel and remove impurities.");

    private final String description;

    Fuel(String description) {
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