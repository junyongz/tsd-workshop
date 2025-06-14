package com.tsd.workshop.workmanship.template.data;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TaskComponentRepository extends R2dbcRepository<TaskComponent, Long> {
}
