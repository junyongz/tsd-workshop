package com.tsd.workshop.vehicle;

public enum Cooling implements Subsystem {
    RADIATOR("Dissipates heat from the engine coolant."),
    COOLING_FAN("Enhances airflow through the radiator."),
    THERMOSTAT("Regulates engine temperature."),
    WATER_PUMP("Circulates coolant through the engine and radiator.");

    private final String description;

    Cooling(String description) {
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
