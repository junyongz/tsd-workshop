package com.tsd.workshop.vehicle;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.vehicle.data.Vehicle;

public class VehicleNoWrongFormatException extends ErrorCodedRuntimeException {
    private Vehicle vehicle;

    public VehicleNoWrongFormatException(Vehicle vehicle) {
        super("Vehicle plate no %s not having proper format".formatted(vehicle.getVehicleNo()));
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public String errorCode() {
        return "VEH-FORMAT-001";
    }
}
