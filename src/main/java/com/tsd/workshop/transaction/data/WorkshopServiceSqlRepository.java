package com.tsd.workshop.transaction.data;

import com.tsd.workshop.transaction.WorkshopServiceNotFoundException;
import com.tsd.workshop.transaction.data.sql.ItemDescKeywords;
import com.tsd.workshop.transaction.data.sql.PartNameKeywords;
import com.tsd.workshop.transaction.data.sql.VehicleNoKeywords;
import io.r2dbc.spi.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
public class WorkshopServiceSqlRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Long> moveToDeletedTable(Long id) {
        return databaseClient.sql("insert into deleted_workshop_service (select * from workshop_service where id = :id)")
                .bind(0, id)
                .flatMap(Result::getRowsUpdated)
                .singleOrEmpty()
                .switchIfEmpty(Mono.just(0L))
                .map(count -> {
                    if (count != 1L) throw new IncorrectResultSizeDataAccessException("expected one but %s".formatted(count), 1);
                    return count;
                })
                .flatMap(insertCount ->
                        databaseClient.sql("""
                                        update deleted_workshop_service set deletion_date = :deletion_date,
                                        spare_part_usages =
                                        (select json_agg(row_to_json(spu)) from spare_part_usages spu where service_id = :service_id)
                                        where id = :id
                                        """)
                                .bind(0, LocalDate.now())
                                .bind(1, id)
                                .bind(2, id)
                                .flatMap(Result::getRowsUpdated)
                                .singleOrEmpty()
                                .switchIfEmpty(Mono.just(0L))
                                .flatMap(updateCount -> {
                                    if (!updateCount.equals(insertCount)) throw new IncorrectResultSizeDataAccessException("expected %s but %s"
                                            .formatted(insertCount, updateCount), insertCount.intValue());
                                    return Mono.just(updateCount);
                                })
                );
    }

    public Mono<WorkshopService> completeWorkshopService(WorkshopService ws) {
        return databaseClient.sql("update workshop_service set completion_date = :completion_date where id = :id")
                .bind(0, ws.getCompletionDate())
                .bind(1, ws.getId())
                .fetch()
                .rowsUpdated()
                .map(count -> {
                    if (count == 0) {
                        throw new WorkshopServiceNotFoundException(ws);
                    }
                    return ws;
                });
    }

    public Mono<WorkshopService> updateNoteFor(WorkshopService ws) {
        return databaseClient.sql("update workshop_service set notes = :notes where id = :id")
                .bind(0, ws.getNotes())
                .bind(1, ws.getId())
                .fetch()
                .rowsUpdated()
                .map(count -> {
                    if (count == 0) {
                        throw new WorkshopServiceNotFoundException(ws);
                    }
                    return ws;
                });
    }

    // to use bindValues to prevent SQL injection
    @Transactional(readOnly = true)
    public Flux<Long> searchServiceIdsByKeywords(List<String> keywords) {
        return databaseClient.sql("""
                select id from workshop_service ws where exists (select 1 from spare_part_usages spu, mig_supplier_spare_parts mssp
                where spu.service_id = ws.id and spu.order_id  = mssp.id
                and %s )
                or exists (select 1 from mig_data where service_id = ws.id and %s)
                or %s
                """.formatted(
                        PartNameKeywords.of(keywords).toSql(),
                        ItemDescKeywords.of(keywords).toSql(),
                        VehicleNoKeywords.of(keywords).toSql()))
                .fetch()
                .all()
                .map(row -> (Long) row.get("id"));
    }
}
