package com.tsd.workshop.transaction.media;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkshopServiceMediaRepository extends R2dbcRepository<WorkshopServiceMedia, Long> {

    @Query("""
            select id, service_id, added_timestamp, file_name, file_size, media_type from workshop_service_media
            where service_id = :service_id
            """)
    Flux<WorkshopServiceMedia> findAllByServiceId(@Param("service_id") Long serviceId);

    @Query("select service_id from workshop_service_media where id = :id")
    Mono<Long> findServiceIdById(@Param("id") Long id);

    Mono<Integer> countByServiceId(Long serviceId);
}
