package com.tsd.workshop.stats.dbtables.data;

import com.tsd.workshop.stats.dbtables.TableTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Transactional(readOnly = true)
@Repository
public class TablesTransactionsR2dbcRepository {

    @Autowired
    private DatabaseClient databaseClient;

    public Flux<TableTransaction> getTablesLastTransactions() {
        return databaseClient.sql("""
                   select 'mig_data' as table_name, MAX(xmin::text::bigint) AS last_trx_id from mig_data
                    union
                    select 'mig_supplier_spare_parts' as table_name, MAX(xmin::text::bigint) AS last_trx_id from mig_supplier_spare_parts
                    union
                    select 'mig_spare_parts' as table_name, MAX(xmin::text::bigint) AS last_trx_id from mig_spare_parts
                    union
                    select 'spare_part_usages' as table_name, MAX(xmin::text::bigint) AS last_trx_id from spare_part_usages
                   """)
                .map(row -> new TableTransaction((String)row.get("table_name"), row.get("last_trx_id", Long.class)))
                .all();
    }
}
