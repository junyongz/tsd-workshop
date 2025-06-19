package com.tsd.workshop.transaction.utilization.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
public interface SparePartUsageRepository extends R2dbcRepository<SparePartUsage, Long> {

    Mono<Void> deleteByServiceId(Long serviceId);

    Mono<Void> deleteByOrderId(Long orderId);

    Flux<SparePartUsage> findByServiceIdOrderByUsageDateDesc(Long serviceId);
}