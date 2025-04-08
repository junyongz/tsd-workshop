package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.data.MigDataRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageR2dbcRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    private MigDataRepository migDataRepository;

    @Transactional
    public Mono<SparePartUsage> saveSparePartUsage(SparePartUsage sparePartUsage) {
        return supplierSparePartRepository.findById(sparePartUsage.getOrderId())
                    .flatMap(ssp -> {
                        MigData migData = ssp.toMigData();
                        migData.afterRecordUsage(sparePartUsage);

                        return migDataRepository.save(migData);
                    })
                    .flatMap(md -> {
                        sparePartUsage.setServiceId(md.getIndex());
                        return sparePartUsageRepository.save(sparePartUsage);
                    });
    }

    public Mono<SparePartUsage> findById(Long id) {
        return sparePartUsageRepository.findById(id);
    }

    public Flux<SparePartUsage> findAll() {
        return sparePartUsageRepository.findAll();
    }

    public Mono<Void> deleteById(Long id) {
        return sparePartUsageRepository.deleteById(id)
                .then(Mono.empty());
    }

    public Mono<Void> deleteByServiceId(Long serviceId) {
        return sparePartUsageRepository.deleteByServiceId(serviceId)
                .then(Mono.empty());
    }

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
                            if (usage > 0) {
                                throw new QuantityNotMatchedException(ssp, usage);
                            }
                            return true;
                        }));
    }

    public Flux<Boolean> validateSparePartUsageByQuantity(List<MigData> migData) {
        Map<Long, Integer> quantityByOrderId = migData.stream().collect(
                Collectors.groupingBy(
                        MigData::getOrderId,
                        HashMap::new,
                        Collectors.summingInt(MigData::getQuantity)));

        // usage quantity PLUS the service quantity, should not exceed the order quantity
       return Flux.fromIterable(quantityByOrderId.entrySet())
                .flatMap(entry -> sparePartUsageR2dbcRepository.usageByOrderId(entry.getKey())
                        .map(sum -> entry.getValue() + sum)
                        .flatMap(quantity ->
                                supplierSparePartR2dbcRepository.quantityById(entry.getKey())
                                        .map(orderQuantity -> {
                                            if (quantity > orderQuantity) {
                                                throw new QuantityOverflowException(quantity, orderQuantity);
                                            }
                                            return true;
                                        })));
    }
}