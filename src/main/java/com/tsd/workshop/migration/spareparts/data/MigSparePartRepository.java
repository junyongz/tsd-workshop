package com.tsd.workshop.migration.spareparts.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MigSparePartRepository extends R2dbcRepository<MigSparePart, Long> {
    // You can add custom queries here if needed
}