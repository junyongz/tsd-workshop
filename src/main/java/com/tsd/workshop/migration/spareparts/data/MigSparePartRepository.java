package com.tsd.workshop.migration.spareparts.data;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
public interface MigSparePartRepository extends R2dbcRepository<MigSparePart, Long> {

    @Query("""
            select sp.id, sp.item_code, sp.part_name, sp.unit, sp.unit_price,
                        -- mssp.id, mssp.quantity, usages.total_quantity,
                        	add_allowed AND mssp.quantity <> coalesce(usages.total_quantity, 0) as add_allowed,
                        	sp.supplier_id, sp.order_id
                        from mig_spare_parts sp
                        left outer join
                          (select order_id, total_quantity from
                        	  (select order_id, sum(quantity) total_quantity from spare_part_usages
                        	     group by order_id) as u) as usages
                          on sp.order_id  = usages.order_id
                        left outer join mig_supplier_spare_parts mssp
                          on sp.order_id  = mssp.id
            """)
    Flux<MigSparePart> findAllAvailableSpareParts();

    Mono<Void> deleteByOrderId(Long orderId);

    Mono<MigSparePart> findBySupplierIdAndOrderId(Long supplierId, Long orderId);
}