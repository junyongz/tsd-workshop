package com.tsd.workshop.transaction;

import com.tsd.workshop.ErrorCodedRuntimeException;

import java.time.LocalDate;

public class VehicleOngoingServiceException extends ErrorCodedRuntimeException {
    public VehicleOngoingServiceException(String vehicleNo, LocalDate startDate) {
        super("there is already a service started since %s for vehicle %s".formatted(vehicleNo, startDate));
    }

    @Override
    public String errorCode() {
        return "VEH-WS-001";
    }
}
