package com.tsd.workshop.vehicle;

public enum Subsystems {

    POWERTRAIN(Powertrain.values()),
    CHASSIS(Chassis.values()),
    BRAKING(Braking.values()),
    ELECTRICAL(Electrical.values()),
    COOLING(Cooling.values()),
    FUEL(Fuel.values()),
    EXHAUST(Exhaust.values()),
    STEERING(Steering.values()),
    CAB(Cabin.values()),
    AIR_SUPPLY(AirSupply.values()),
    TRAILER(Trailer.values()),
    SAFETY_AND_MONITORING(SafetyAndMonitoring.values()),
    LUBRICATION(Lubrication.values());

    private Subsystem[] subsystems;

    Subsystems(Subsystem[] subsystems) {
        this.subsystems = subsystems;
    }
}
