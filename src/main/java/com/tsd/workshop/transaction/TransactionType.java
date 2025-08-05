package com.tsd.workshop.transaction;

/**
 * Either Repair, Service, Inspection and/or Tyre
 */
public enum TransactionType {

    REPAIR("General repair service, including accident repair"),

    SERVICE("Scheduled maintenance service, such as engine oil, filter"),

    INSPECTION("General work for inspection purpose"),

    TYRE("Change tyres");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
