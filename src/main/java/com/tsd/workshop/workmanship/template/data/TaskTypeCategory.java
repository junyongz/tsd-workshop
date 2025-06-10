package com.tsd.workshop.workmanship.template.data;

public enum TaskTypeCategory {
    INSPECTION("Checks components for damage, wear, or issues to ensure functionality"),
    LUBRICATION("Applies grease or oil to reduce friction and wear in moving parts"),
    CLEANING("Removes dirt, grime, or contaminants to maintain performance and hygiene"),
    REPAIR("Fixes or adjusts components to restore proper operation and reliability"),
    REPLACEMENT("Replaces worn or damaged parts with new ones to ensure functionality"),
    OVERHAUL("Rebuilds major components to restore performance, replacing multiple parts");

    private final String description;

    TaskTypeCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
