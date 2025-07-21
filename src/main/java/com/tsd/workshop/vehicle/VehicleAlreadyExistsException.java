package com.tsd.workshop.vehicle;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.vehicle.data.Vehicle;

public class VehicleAlreadyExistsException extends ErrorCodedRuntimeException {
    public VehicleAlreadyExistsException(Vehicle veh) {
        super("vehicle %s already exists in the system".formatted(veh));
    }

    @Override
    public String errorCode() {
        return "VEH-EXISTS";
    }
}
