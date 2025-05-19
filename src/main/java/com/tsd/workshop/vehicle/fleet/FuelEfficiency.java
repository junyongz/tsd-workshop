package com.tsd.workshop.vehicle.fleet;

public class FuelEfficiency {

    private final String vehicleNo;

    private final Double fuelUsageLitrePerKm;

    public FuelEfficiency(String vehicleNo, Double fuelUsageLitrePerKm) {
        this.vehicleNo = vehicleNo;
        this.fuelUsageLitrePerKm = fuelUsageLitrePerKm;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public Double getFuelUsageLitrePerKm() {
        return fuelUsageLitrePerKm;
    }
}
