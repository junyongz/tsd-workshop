package com.tsd.workshop.workmanship.template.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TaskComponentRepository extends R2dbcRepository<TaskComponent, Long> {
}
