package com.tsd.workshop.migration.tasks.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Transactional(readOnly = true)
@Repository
public class MigTaskSqlRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Flux<MigTask> searchByKeyword(String keyword) {
       return this.databaseClient.sql("""
                select description, max(unit_price) unit_price, count(*) cnt
                  from raw_tasks_unit_price
                 where lower(description) like :keyword
                   and unit_price is not null
                  group by description
                  order by cnt desc, unit_price asc
                """)
               .bind("keyword", "%%%s%%".formatted(keyword.toLowerCase()))
               .fetch()
               .all()
               .map(row -> {
                    MigTask task = new MigTask();
                    task.setDescription(row.get("description").toString());
                    task.setUnitPrice((BigDecimal) row.get("unit_price"));
                    return task;
               });
    }

    public Flux<MigTask> searchByTasks(String workshopTasks, String subsystem) {
        return this.databaseClient.sql("""
                select * from (
                select description, max(unit_price) unit_price, count(*) cnt
                  from raw_tasks_unit_price
                 where workshop_tasks @> :tasks::text[]
                   and subsystem = :subsystem
                   and unit_price is not null
                 group by description
                union all
                select description, max(unit_price) unit_price, count(*) cnt
                  from raw_tasks_unit_price
                 where to_tsvector(description) @@ websearch_to_tsquery(:single_tasks)
                   and unit_price is not null
                 group by description
                 ) t order by cnt desc, unit_price asc
                """)
                .bind("tasks", new String[] { workshopTasks })
                .bind("single_tasks", workshopTasks)
                .bind("subsystem", subsystem)
                .fetch()
                .all()
                .map(row -> {
                    MigTask task = new MigTask();
                    task.setDescription(row.get("description").toString());
                    task.setUnitPrice((BigDecimal) row.get("unit_price"));
                    return task;
                });
    }

    public Flux<MigTask> findAll() {
        return this.databaseClient.sql("""
                select description, max(unit_price) as unit_price, count(*) cnt
                  from raw_tasks_unit_price
                 where unit_price is not null
                 group by description
                 order by cnt desc, unit_price asc
                """)
                .fetch()
                .all()
                .map(row -> {
                    MigTask task = new MigTask();
                    task.setDescription(row.get("description").toString());
                    task.setUnitPrice((BigDecimal) row.get("unit_price"));
                    return task;
                });
    }
}
