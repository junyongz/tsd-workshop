package com.tsd.workshop.migration.data;

import io.r2dbc.spi.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class MigDataJdbcRepository {

    @Autowired
    private DatabaseClient databaseClient;

    @Transactional
    public Mono<Long> moveToDeletedTable(Long id) {
        return databaseClient.sql("insert into deleted_mig_data (select * from mig_data where index = :index)")
                .bind(0, id)
                .flatMap(Result::getRowsUpdated)
                .map(count -> {
                    if (count != 1L) throw new IncorrectResultSizeDataAccessException("expected one but %s".formatted(count), 1);
                    return count;
                })
                .flatMap(insertCount ->
                    databaseClient.sql("update deleted_mig_data set deletion_date = :deletionDate where index = :index")
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
                    databaseClient.sql("delete from mig_data where index = :index")
                            .bind(0, id)
                            .flatMap(Result::getRowsUpdated)
                            .flatMap(deleteCount -> {
                                if (!deleteCount.equals(updateCount)) throw new IncorrectResultSizeDataAccessException("expected %s but %s"
                                        .formatted(updateCount, deleteCount), updateCount.intValue());
                                return Mono.just(deleteCount);
                            })
                            .singleOrEmpty()
                )
                .next();
    }

}
