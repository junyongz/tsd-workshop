package com.tsd.workshop.transaction.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Map;

@Transactional(readOnly = true)
@Component
public class WorkshopServiceMediaSqlRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Map<Long, Integer>> groupedServiceIdCounts() {
        // this would return empty map if there is no result emitted, check 'collectMap' javadoc
        return this.databaseClient.sql("select service_id, count(*) cnt from workshop_service_media group by service_id")
                .fetch()
                .all()
                .collectMap(
                        row -> (Long) row.get("service_id"),
                        row -> ((Number) row.get("cnt")).intValue()
                );
    }
}
