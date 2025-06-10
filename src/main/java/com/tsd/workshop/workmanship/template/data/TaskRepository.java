package com.tsd.workshop.workmanship.template.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TaskRepository extends R2dbcRepository<Task, Long> {
}
