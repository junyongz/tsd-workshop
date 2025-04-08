package com.tsd.workshop.vehicle;

public enum AirSupply implements Subsystem {
    AIR_COMPRESSOR("Generates compressed air for pneumatic systems."),
    AIR_DRYER("Removes moisture from compressed air."),
    AIR_RESERVOIRS("Store compressed air for immediate use.");

    private final String description;

    AirSupply(String description) {
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
