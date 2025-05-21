package com.tsd.workshop.migration.suppliers.data;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SupplierSparePartRepository extends R2dbcRepository<SupplierSparePart, Long> {

    @Query("""
            select * from mig_supplier_spare_parts spp where exists
            (select 1 from spare_part_usages where order_id = spp.id)
            order by invoice_date desc, delivery_order_no desc
            """)
    Flux<SupplierSparePart> findWithUsageSupplierSparePartsBy();
}
