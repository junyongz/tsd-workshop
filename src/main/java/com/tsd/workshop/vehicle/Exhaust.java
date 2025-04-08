package com.tsd.workshop.vehicle;

public enum Exhaust implements Subsystem {
    EXHAUST_MANIFOLD("Collects exhaust gases from the engine."),
    TURBOCHARGER("Increases engine power with extra air."),
    EXHAUST_PIPE("Directs gases away from the engine."),
    DIESEL_PARTICULATE_FILTER("Reduces particulate emissions."),
    SELECTIVE_CATALYTIC_REDUCTION("Reduces nitrogen oxide emissions.");

    private final String description;

    Exhaust(String description) {
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
