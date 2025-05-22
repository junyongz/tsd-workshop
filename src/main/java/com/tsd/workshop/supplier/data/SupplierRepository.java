package com.tsd.workshop.supplier.data;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SupplierRepository extends R2dbcRepository<Supplier, Long> {

    @Query("""
            select * from (
            select id, supplier_name,
            (select max(invoice_date) from mig_supplier_spare_parts where supplier_id = supplier.id) latest_invoice_date
            from supplier) s order by latest_invoice_date desc
            """)
    Flux<Supplier> findByRecentOrdered();
}