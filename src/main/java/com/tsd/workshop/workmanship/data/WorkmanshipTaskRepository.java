package com.tsd.workshop.workmanship.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
public interface WorkmanshipTaskRepository extends R2dbcRepository<WorkmanshipTask, Long> {

    Flux<WorkmanshipTask> findByServiceId(Long serviceId);

    Mono<Void> deleteByServiceId(Long serviceId);
}
