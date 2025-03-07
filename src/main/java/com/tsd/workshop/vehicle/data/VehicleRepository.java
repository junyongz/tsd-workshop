package com.tsd.workshop.vehicle.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface VehicleRepository extends R2dbcRepository<Vehicle, Long> {
    // You can add custom queries here if needed
}
