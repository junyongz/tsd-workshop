package com.tsd.workshop.migration.suppliers.data;

import io.r2dbc.spi.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class SupplierSparePartR2dbcRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Long> updateNotes(SupplierSparePart sparePart) {
        return databaseClient.sql("update mig_supplier_spare_parts set notes = :notes where id = :id")
                .bind(0, sparePart.getNotes())
                .bind(1, sparePart.getId())
                .flatMap(Result::getRowsUpdated)
                .singleOrEmpty();
    }

    public Mono<BigDecimal> quantityById(Long orderId) {
        return databaseClient.sql("select coalesce(quantity,0) quantity from mig_supplier_spare_parts where id = :order_id")
                .bind(0, orderId)
                .map(row -> row.get("quantity", BigDecimal.class))
                .first();
    }

    public Mono<Long> deplete(Long orderId) {
        return databaseClient.sql("update mig_supplier_spare_parts set status = 'DEPLETED' where id = :order_id")
                .bind(0, orderId)
                .flatMap(Result::getRowsUpdated)
                .singleOrEmpty();
    }

    @Transactional
    public Mono<Long> updateWithSparePartId(Long sparePartId, List<Long> orderIds) {
        return databaseClient.sql("update mig_supplier_spare_parts set spare_part_id = null where spare_part_id = :spare_part_id")
                        .bind("spare_part_id", sparePartId)
                        .flatMap(Result::getRowsUpdated)
                        .then(databaseClient.sql("update mig_supplier_spare_parts set spare_part_id = :spare_part_id where id in (:order_ids)")
                                .bind("spare_part_id", sparePartId)
                                .bind("order_ids", orderIds)
                                .flatMap(Result::getRowsUpdated)
                                .singleOrEmpty());
    }

    @Transactional
    public Mono<Long> updateSparePartIdToNull(Long sparePartId) {
        return databaseClient.sql("update mig_supplier_spare_parts set spare_part_id = null where spare_part_id = :spare_part_id")
                        .bind("spare_part_id", sparePartId)
                        .flatMap(Result::getRowsUpdated)
                        .singleOrEmpty();
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
