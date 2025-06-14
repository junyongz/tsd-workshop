package com.tsd.workshop.migration.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Repository
@Transactional(readOnly = true)
public interface MigDataRepository extends R2dbcRepository<MigData, Long> {

    Flux<MigData> findByServiceId(Long serviceId);

}