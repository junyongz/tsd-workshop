package com.tsd.workshop.vehicle.data;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface VehicleRepository extends R2dbcRepository<Vehicle, Long> {
    // You can add custom queries here if needed

    @Query("""
    select * from vehicle where company_id in (select id from company where internal = true)
    order by latest_mileage_km desc, plate_no desc
    """)
    Flux<Vehicle> findAllOfInternal();
}
