package com.tsd.workshop.migration.suppliers;

import com.tsd.workshop.migration.suppliers.data.Status;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartSqlRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Transactional
@Service
public class SupplierSparePartService {
    @Autowired
    private SupplierSparePartRepository supplierSparePartRepository;

    @Autowired
    private SupplierSparePartSqlRepository supplierSparePartSqlRepository;

    @Autowired
    private SparePartUsageRepository sparePartUsageRepository;

    public Flux<SupplierSparePart> saveSupplierSpareParts(List<SupplierSparePart> supplierSpareParts) {
        if (StringUtils.hasText(supplierSpareParts.getFirst().getDeliveryOrderNo())) {
            return supplierSparePartRepository.saveAll(supplierSpareParts);
        }

        return supplierSparePartRepository.saveAll(supplierSpareParts)
                .collectList()
                .flatMapMany(parts -> {
                    Long firstId = parts.getFirst().getId();
                    for (SupplierSparePart ssp : parts) {
                        ssp.setDeliveryOrderNo("PENDING-DO-"+firstId);
                    }
                    return supplierSparePartRepository.saveAll(parts);
                });
    }

    public Mono<SupplierSparePart> updateNotes(SupplierSparePart spp) {
        return supplierSparePartSqlRepository.updateNotes(spp)
                .flatMap(count -> {
                    if (count == 0) {
                        throw new SupplierSparePartNotFoundException(spp);
                    }
                    return Mono.just(spp);
                });
    }

    public Mono<SupplierSparePart> deplete(SupplierSparePart spp) {
        return supplierSparePartSqlRepository.deplete(spp.getId())
                .flatMap(count -> {
                    if (count == 0) {
                        throw new SupplierSparePartNotFoundException(spp);
                    }
                    spp.setStatus(Status.DEPLETED);
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
        return supplierSparePartSqlRepository.moveToDeletedTable(id)
                .flatMap(count -> sparePartUsageRepository.deleteByOrderId(id));
    }
}
