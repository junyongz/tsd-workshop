package com.tsd.workshop.transaction.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WorkshopServiceRepository extends R2dbcRepository<WorkshopService, Long> {
}
