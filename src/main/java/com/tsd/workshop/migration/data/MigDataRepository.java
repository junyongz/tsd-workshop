package com.tsd.workshop.migration.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MigDataRepository extends R2dbcRepository<MigData, Long> {

}