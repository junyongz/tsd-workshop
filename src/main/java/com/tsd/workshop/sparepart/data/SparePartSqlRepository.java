package com.tsd.workshop.sparepart.data;

import com.tsd.workshop.sql.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@Transactional
public class SparePartSqlRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Flux<Long> searchByKeywords(List<String> keywords, int pageNumber, int pageSize) {
        return databaseClient
                .sql("select id from spare_part where %s order by creation_date desc, id desc %s"
                        .formatted(Keywords.toSql(keywords), Pagination.toSql(pageNumber, pageSize)))
                .bindValues(Keywords.toBindValues(keywords))
                .fetch()
                .all()
                .map(row -> (Long) row.get("id"));
    }

    public Mono<Long> countByKeywords(List<String> keywords) {
        return databaseClient
                .sql("select count(*) cnt from spare_part where %s"
                        .formatted(Keywords.toSql(keywords)))
                .bindValues(Keywords.toBindValues(keywords))
                .fetch()
                .first()
                .map(row -> (Long) row.get("cnt"));
    }
}
