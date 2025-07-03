package com.tsd.workshop.sparepart.media.data;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
public interface SparePartMediaRepository extends R2dbcRepository<SparePartMedia, Long> {

    @Query("""
            select id, spare_part_id, added_timestamp, file_name, file_size, media_type from spare_part_media
            where spare_part_id = :spare_part_id
            """)
    Flux<SparePartMedia> findAllBySparePartId(@Param("spare_part_id") Long sparePartId);

    @Query("select spare_part_id from spare_part_media where id = :id")
    Mono<Long> findSparePartIdById(@Param("id") Long id);

    Mono<Integer> countBySparePartId(Long sparePartId);

    Mono<Void> deleteBySparePartId(Long sparePartId);
}
