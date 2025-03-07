package com.tsd.workshop.company.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CompanyRepository extends R2dbcRepository<Company, Long> {
    // You can add custom queries here if needed
}