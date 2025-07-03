package com.tsd.workshop.sparepart.media.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Map;

@Transactional(readOnly = true)
@Repository
public class SparePartMediaSqlRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Map<Long, Integer>> groupedSparePartIdCounts() {
        // this would return empty map if there is no result emitted, check 'collectMap' javadoc
        return this.databaseClient.sql("select expense_id, count(*) cnt from expense_attachment group by expense_id")
                .fetch()
                .all()
                .collectMap(
                        row -> (Long) row.get("expense_id"),
                        row -> ((Number) row.get("cnt")).intValue()
                );
    }
}
