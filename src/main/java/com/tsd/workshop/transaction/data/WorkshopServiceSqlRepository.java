package com.tsd.workshop.transaction.data;

import io.r2dbc.spi.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class WorkshopServiceSqlRepository {

    @Autowired
    private DatabaseClient databaseClient;

    @Transactional
    public Mono<Long> moveToDeletedTable(Long id) {
        return databaseClient.sql("insert into deleted_workshop_service (select * from workshop_service where id = :id)")
                .bind(0, id)
                .flatMap(Result::getRowsUpdated)
                .singleOrEmpty()
                .map(count -> {
                    if (count != 1L) throw new IncorrectResultSizeDataAccessException("expected one but %s".formatted(count), 1);
                    return count;
                })
                .flatMap(insertCount ->
                        databaseClient.sql("update deleted_workshop_service set deletion_date = :deletion_date where id = :id")
                                .bind(0, LocalDate.now())
                                .bind(1, id)
                                .flatMap(Result::getRowsUpdated)
                                .flatMap(updateCount -> {
                                    if (!updateCount.equals(insertCount)) throw new IncorrectResultSizeDataAccessException("expected %s but %s"
                                            .formatted(insertCount, updateCount), insertCount.intValue());
                                    return Mono.just(updateCount);
                                })
                                .singleOrEmpty()
                )
                .flatMap(updateCount ->
                        databaseClient.sql("""
                                        update deleted_workshop_service set spare_part_usages =
                                        (select json_agg(row_to_json(spu)) from spare_part_usages spu where service_id = :service_id)
                                        where id = :id
                                        """)
                                .bind(0, id)
                                .bind(1, id)
                                .flatMap(Result::getRowsUpdated)
                                .singleOrEmpty()
                                .flatMap(jsonUpdateCount -> {
                                    if (!jsonUpdateCount.equals(updateCount)) throw new IncorrectResultSizeDataAccessException("expected %s but %s"
                                            .formatted(updateCount, jsonUpdateCount), updateCount.intValue());
                                    return Mono.just(jsonUpdateCount);
                                })
                );
    }
}
