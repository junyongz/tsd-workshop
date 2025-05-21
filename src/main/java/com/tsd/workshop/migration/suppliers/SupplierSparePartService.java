package com.tsd.workshop.migration.suppliers;

import com.tsd.workshop.migration.spareparts.MigSparePartService;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
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
    private SparePartUsageRepository sparePartUsageRepository;

    public Flux<SupplierSparePart> saveSupplierSpareParts(List<SupplierSparePart> supplierSpareParts) {
        return supplierSparePartRepository
                    .saveAll(supplierSpareParts)
                    .collectList()
                    .flatMapMany(sss ->
                        migSparePartService.smartSaveMigSpareParts(sss).thenMany(Flux.fromIterable(sss))
                    );
    }

    public Mono<SupplierSparePart> updateNotes(SupplierSparePart spp) {
        return supplierSparePartR2dbcRepository.updateNotes(spp)
                .flatMap(count -> {
                    if (count == 0) {
                        throw new SupplierSparePartNotFoundException(spp);
                    }
                    return Mono.just(spp);
                });
    }

    public Mono<SupplierSparePart> findById(Long id) {
        return supplierSparePartRepository.findById(id);
    }

    public Flux<SupplierSparePart> findAll() {
        return supplierSparePartRepository.findAll(Sort.by(
                Sort.Order.desc("invoiceDate"),
                Sort.Order.desc("deliveryOrderNo")));
    }

    public Flux<SupplierSparePart> findWithUsage() {
        return supplierSparePartRepository.findWithUsageSupplierSparePartsBy();
    }

    @Transactional
    public Mono<Void> deleteById(Long id) {
        return supplierSparePartR2dbcRepository.moveToDeletedTable(id)
                .flatMap(count -> migSparePartService.deleteByOrderId(id))
                .then(sparePartUsageRepository.deleteByOrderId(id));
    }
}
