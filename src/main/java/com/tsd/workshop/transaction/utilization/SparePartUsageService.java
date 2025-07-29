package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.data.MigDataSqlRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartRepository;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.data.WorkshopServiceRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageR2dbcRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SparePartUsageService {
    @Autowired
    private SparePartUsageRepository sparePartUsageRepository;

    @Autowired
    private SparePartUsageR2dbcRepository sparePartUsageR2dbcRepository;

    @Autowired
    private SupplierSparePartRepository supplierSparePartRepository;

    @Autowired
    private SupplierSparePartR2dbcRepository supplierSparePartR2dbcRepository;

    @Autowired
    private WorkshopServiceRepository workshopServiceRepository;

    @Autowired
    private MigDataSqlRepository migDataSqlRepository;

    @Transactional
    public Mono<SparePartUsage> saveSparePartUsage(SparePartUsage sparePartUsage) {
        if (sparePartUsage.getServiceId() == null) {
            WorkshopService ws = sparePartUsage.toWorkshopService();

            return workshopServiceRepository.save(ws)
                    .flatMap(saved -> {
                        sparePartUsage.setServiceId(saved.getId());
                        return sparePartUsageRepository.save(sparePartUsage);
                    });
        }
        return sparePartUsageRepository.save(sparePartUsage);
    }

    public Mono<SparePartUsage> findById(Long id) {
        return sparePartUsageRepository.findById(id);
    }

    public Flux<SparePartUsage> findAll() {
        return sparePartUsageRepository.findAll();
    }

    @Transactional
    public Mono<Long> deleteById(Long id) {
        return sparePartUsageRepository.deleteById(id)
                .then(Mono.just(id));
    }

    @Transactional
    public Mono<Void> deleteByServiceId(Long serviceId) {
        return sparePartUsageRepository.deleteByServiceId(serviceId)
                .then(Mono.empty());
    }

    @Transactional
    public Flux<SparePartUsage> saveAllSparePartUsages(Iterable<SparePartUsage> sparePartUsages) {
        return sparePartUsageRepository.saveAll(sparePartUsages);
    }

    /**
     * To validate whether any already-created spare part been used, so not to mess up the quantity
     * @param supplierSpareParts already-created spare parts
     * @return whether it has been used for any created spare part
     */
    public Flux<Boolean> validateSparePartUsageQuantityForEditing(List<SupplierSparePart> supplierSpareParts) {
        if (supplierSpareParts.isEmpty()) {
            return Flux.just(true);
        }

        return Flux.fromIterable(supplierSpareParts)
                .flatMap(ssp -> sparePartUsageR2dbcRepository.usageByOrderId(ssp.getId())
                        .map(usage -> {
                            if (usage.compareTo(BigDecimal.ZERO) > 0) {
                                throw new QuantityNotMatchedException(ssp, usage);
                            }
                            return true;
                        }));
    }

    public Flux<Boolean> validateSparePartUsageByQuantity(List<SparePartUsage> sparePartUsages) {
        Map<Long, Double> quantityByOrderId = sparePartUsages.stream().collect(
                Collectors.groupingBy(
                        SparePartUsage::getOrderId,
                        HashMap::new,
                        Collectors.summingDouble(spu -> spu.getQuantity() != null ? spu.getQuantity().doubleValue() : 0.0d)));

        return doValidateSparePartUsageByQuantity(quantityByOrderId);
    }

    public Flux<Boolean> validateMigDataSparePartUsageByQuantity(List<MigData> migData) {
        Map<Long, Double> quantityByOrderId = migData.stream().collect(
                Collectors.groupingBy(
                        MigData::getOrderId,
                        HashMap::new,
                        Collectors.summingDouble(md -> md.getQuantity() != null ? md.getQuantity().doubleValue() : 0.0d)));

        // usage quantity PLUS the service quantity, should not exceed the order quantity
        return doValidateSparePartUsageByQuantity(quantityByOrderId);
    }

    public Flux<Boolean> doValidateSparePartUsageByQuantity(Map<Long, Double> quantityByOrderId) {
        return Flux.fromIterable(quantityByOrderId.entrySet())
                .flatMap(entry -> sparePartUsageR2dbcRepository.usageByOrderId(entry.getKey())
                        .map(sum -> sum.add(BigDecimal.valueOf(entry.getValue())))
                        .flatMap(quantity ->
                                supplierSparePartR2dbcRepository.quantityById(entry.getKey())
                                        .map(orderQuantity -> {
                                            if (quantity.compareTo(orderQuantity) > 0) {
                                                throw new QuantityOverflowException(quantity, orderQuantity);
                                            }
                                            return true;
                                        })));
    }

    public Mono<SparePartUsage> migrateFromHandWritten(SparePartUsage sparePartUsage) {
        if (sparePartUsage.getMigDataIndex() == null) {
            throw new IllegalArgumentException("this routine is for migration from hand written part, but is empty");
        }
        return sparePartUsageRepository.save(sparePartUsage)
                .flatMap(spu -> {
                    return migDataSqlRepository.nullifyServiceIdPostMigration(spu.getMigDataIndex())
                            .then(Mono.just(spu));
                });
    }
}