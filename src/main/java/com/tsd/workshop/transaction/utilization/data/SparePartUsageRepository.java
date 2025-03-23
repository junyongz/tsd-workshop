package com.tsd.workshop.transaction.utilization.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface SparePartUsageRepository extends R2dbcRepository<SparePartUsage, Long> {

    Mono<Void> deleteByServiceId(Long serviceId);

}