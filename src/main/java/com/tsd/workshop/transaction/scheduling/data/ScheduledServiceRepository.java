package com.tsd.workshop.transaction.scheduling.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Transactional
@Repository
public interface ScheduledServiceRepository extends R2dbcRepository<ScheduledService, Long> {

    Flux<ScheduledService> findByScheduledDateAfter(LocalDate localDate);
}
