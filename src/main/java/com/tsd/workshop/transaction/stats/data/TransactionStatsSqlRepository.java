package com.tsd.workshop.transaction.stats.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Repository
@Transactional
public class TransactionStatsSqlRepository {

    private final String selectStatement = """
            select (select company_name from company where id = veh.company_id) company_name,
              sum(main.part_costs) part_costs, sum(tasks_costs) task_costs,
              start_date, count(*),
              count(*) filter (where 'REPAIR' = any(transaction_types)) as repair_count,
              count(*) filter (where 'SERVICE' = any(transaction_types)) as service_count,
              count(*) filter (where 'INSPECTION' = any(transaction_types)) as inspection_count,
              count(*) filter (where 'TYRE' = any(transaction_types)) as tyre_count,
              count(*) filter (where completion_date is null) as pending_count,
              count(*) filter (where completion_date is not null) as completion_count,
              round(avg(duration)) average_completion_days
                from (select *, (completion_date - start_date) duration,
                 (select sum(spu.quantity * spu.sold_price) from spare_part_usages spu
                   where spu.service_id  = ws.id) part_costs,
                 (select sum(wt.quoted_price ) from workmanship_task wt
                   where wt.service_id  = ws.id) tasks_costs
                    from workshop_service ws) main
              inner join vehicle veh on main.vehicle_id  = veh.id
            """;

    private final String forTargetedDate = """
            insert into service_summary (%s where start_date = :target_date
                and not exists (select 1 from service_summary where start_date = main.start_date)
                group by start_date, veh.company_id)
            """.formatted(selectStatement);

    private final String brandNew = """
             insert into service_summary (%s
             group by start_date, veh.company_id
             order by start_date asc)
            """.formatted(selectStatement);

    @Autowired
    private DatabaseClient databaseClient;

    @Transactional(readOnly = true)
    public Flux<Map<String, Object>> queryByStartDateRange(LocalDate fromDate, LocalDate toDate) {
        return databaseClient.sql("""
                        select * from service_summary
                         where start_date >= :from_date
                           and start_date <= :to_date
                         order by start_date asc
                        """)
                .bind("from_date", fromDate)
                .bind("to_date", toDate)
                .fetch()
                .all();
    }

    public Mono<Long> insertForDate(LocalDate targetedDate) {
        return databaseClient.sql(forTargetedDate)
                .bind("target_date", targetedDate)
                .fetch()
                .rowsUpdated();
    }

    public Mono<Long> cleanInstall() {
        return databaseClient.sql("truncate table service_summary")
                .fetch()
                .rowsUpdated()
                .flatMap(count -> databaseClient.sql(brandNew)
                        .fetch().rowsUpdated());
    }
}
