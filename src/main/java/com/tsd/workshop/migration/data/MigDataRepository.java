package com.tsd.workshop.migration.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface MigDataRepository extends R2dbcRepository<MigData, Long> {

    Flux<MigData> findByServiceId(Long serviceId);

}