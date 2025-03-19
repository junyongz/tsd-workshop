package com.tsd.workshop.migration.suppliers.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SupplierSparePartRepository extends R2dbcRepository<SupplierSparePart, Long> {
    // You can add custom queries here if needed
}
