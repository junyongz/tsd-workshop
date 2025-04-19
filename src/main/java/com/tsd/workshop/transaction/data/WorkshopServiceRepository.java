package com.tsd.workshop.transaction.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface WorkshopServiceRepository extends R2dbcRepository<WorkshopService, Long> {

    Flux<WorkshopService> findByVehicleIdAndCompletionDateIsNull(Long vehicleId);
}
