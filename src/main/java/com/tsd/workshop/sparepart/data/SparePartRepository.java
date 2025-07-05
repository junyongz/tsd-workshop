package com.tsd.workshop.sparepart.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Repository
@Transactional
public interface SparePartRepository extends R2dbcRepository<SparePart, Long> {

    Flux<SparePart> findAllBy(Pageable pageable);
}
