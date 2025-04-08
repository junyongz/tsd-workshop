package com.tsd.workshop.vehicle;

public enum SafetyAndMonitoring implements Subsystem {
    COLLISION_AVOIDANCE_SYSTEM("Detects obstacles using radar or cameras."),
    LANE_DEPARTURE_WARNING("Alerts driver if truck drifts out of lane."),
    TIRE_PRESSURE_MONITORING_SYSTEM("Tracks tire pressure for safety."),
    BACKUP_CAMERA_SENSORS("Assists with reversing and parking."),
    TELEMATICS_SYSTEM("Tracks location and vehicle performance.");

    private final String description;

    SafetyAndMonitoring(String description) {
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