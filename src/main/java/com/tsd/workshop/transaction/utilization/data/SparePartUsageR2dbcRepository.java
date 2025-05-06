package com.tsd.workshop.transaction.utilization.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Transactional(readOnly = true)
@Repository
public class SparePartUsageR2dbcRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<BigDecimal> usageByOrderId(Long orderId) {
        return databaseClient.sql("select coalesce(sum(quantity), 0) total_quantity from spare_part_usages where order_id = :order_id")
                .bind(0, orderId)
                .map(row -> row.get("total_quantity", BigDecimal.class))
                .first();
    }
}
