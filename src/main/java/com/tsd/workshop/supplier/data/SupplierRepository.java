package com.tsd.workshop.supplier.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface SupplierRepository extends R2dbcRepository<Supplier, Long> {
    // You can add custom queries here if needed
}