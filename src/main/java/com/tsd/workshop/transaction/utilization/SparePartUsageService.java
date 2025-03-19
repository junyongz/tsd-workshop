package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SparePartUsageService {
    @Autowired
    private SparePartUsageRepository sparePartUsageRepository;

    public Mono<SparePartUsage> saveSparePartUsage(SparePartUsage sparePartUsage) {
        if (sparePartUsage.getId() != null) {
            sparePartUsage.setId(null); // Force id to be null for auto-increment
        }
        return sparePartUsageRepository.save(sparePartUsage);
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
        return sparePartUsageRepository.deleteByServiceId(serviceId).then(Mono.empty());
    }

    public Flux<SparePartUsage> saveAllSparePartUsages(Iterable<SparePartUsage> sparePartUsages) {
        return sparePartUsageRepository.saveAll(sparePartUsages);
    }
}