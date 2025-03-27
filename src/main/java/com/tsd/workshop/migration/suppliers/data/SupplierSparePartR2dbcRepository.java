package com.tsd.workshop.migration.suppliers.data;

import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import io.r2dbc.spi.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class SupplierSparePartR2dbcRepository {

    @Autowired
    private DatabaseClient databaseClient;

    // TODO to clear this afterward, just greedy with the SQL here.
    public Flux<Long> updateUsageNote(List<SparePartUsage> usages) {
        return Flux.fromIterable(usages).flatMap(usage ->
                databaseClient.sql("update mig_supplier_spare_parts set notes = coalesce(notes, '') || " +
                                "case when notes is null then '' else chr(10) end || :notes " +
                                "where id = :id")
                        .bind(0, "Used by: %s @ %s".formatted(usage.getVehicleNo(), usage.getUsageDate()))
                        .bind(1, usage.getOrderId())
                        .flatMap(Result::getRowsUpdated)
        );
    }

    public Mono<Integer> quantityById(Long orderId) {
        return databaseClient.sql("select coalesce(quantity,0) quantity from mig_supplier_spare_parts where id = :order_id")
                .bind(0, orderId)
                .map(row -> row.get("quantity", Integer.class))
                .first();
    }

    @Transactional
    public Mono<Long> moveToDeletedTable(Long orderId) {
        // TODO duplicate code with deletion of mig_data
        return databaseClient.sql("insert into deleted_mig_supplier_spare_parts (select * from mig_supplier_spare_parts where id = :id)")
                .bind(0, orderId)
                .flatMap(Result::getRowsUpdated)
                .map(count -> {
                    if (count != 1L) throw new IncorrectResultSizeDataAccessException("expected one but %s".formatted(count), 1);
                    return count;
                })
                .flatMap(insertCount ->
                        databaseClient.sql("update deleted_mig_supplier_spare_parts set deletion_date = :deletionDate where id = :id")
                                .bind(0, LocalDate.now())
                                .bind(1, orderId)
                                .flatMap(Result::getRowsUpdated)
                                .flatMap(updateCount -> {
                                    if (!updateCount.equals(insertCount)) throw new IncorrectResultSizeDataAccessException("expected %s but %s"
                                            .formatted(insertCount, updateCount), insertCount.intValue());
                                    return Mono.just(updateCount);
                                })
                                .singleOrEmpty()
                )
                .flatMap(updateCount ->
                        databaseClient.sql("delete from mig_supplier_spare_parts where id = :id")
                                .bind(0, orderId)
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
