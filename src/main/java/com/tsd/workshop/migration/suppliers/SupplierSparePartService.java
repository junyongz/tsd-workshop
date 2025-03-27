package com.tsd.workshop.migration.suppliers;

import com.tsd.workshop.migration.MigDataService;
import com.tsd.workshop.migration.spareparts.MigSparePartService;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Transactional
@Service
public class SupplierSparePartService {
    @Autowired
    private SupplierSparePartRepository supplierSparePartRepository;

    @Autowired
    private SupplierSparePartR2dbcRepository supplierSparePartR2dbcRepository;

    @Autowired
    private MigSparePartService migSparePartService;

    @Autowired
    private MigDataService migDataService;

    public Flux<SupplierSparePart> saveSupplierSpareParts(List<SupplierSparePart> supplierSpareParts) {
        return supplierSparePartRepository
                    .saveAll(supplierSpareParts)
                    .collectList()
                    .flatMapMany(sss ->
                        migSparePartService.saveMigSparePartsForNewOrder(sss)
                                .thenMany(Flux.fromIterable(sss))
                    );
    }

    public Mono<SupplierSparePart> findById(Long id) {
        return supplierSparePartRepository.findById(id);
    }

    public Flux<SupplierSparePart> findAll() {
        return supplierSparePartRepository.findAll(Sort.by(
                Sort.Order.desc("invoiceDate"),
                Sort.Order.desc("deliveryOrderNo")));
    }

    @Transactional
    public Mono<Void> deleteById(Long id) {
        // TODO to delete related service that using this order as well, and mig_spare_parts too
        return supplierSparePartR2dbcRepository.moveToDeletedTable(id)
                .flatMap(count -> migDataService.deleteByOrderId(id))
                .flatMap(count -> migSparePartService.deleteByOrderId(id));
    }
}
