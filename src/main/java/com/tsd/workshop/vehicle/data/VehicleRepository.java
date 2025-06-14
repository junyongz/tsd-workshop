package com.tsd.workshop.vehicle.data;

import com.tsd.workshop.vehicle.VehicleStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Repository
@Transactional
public interface VehicleRepository extends R2dbcRepository<Vehicle, Long> {
    // You can add custom queries here if needed

    @Transactional(readOnly = true)
    @Query("""
    select * from vehicle where company_id in (select id from company where internal = true)
    and (status = :vehicleStatus or status is null)
    order by latest_mileage_km desc, plate_no desc
    """)
    Flux<Vehicle> findAllOfInternal(VehicleStatus vehicleStatus);
}
