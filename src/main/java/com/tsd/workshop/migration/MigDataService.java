package com.tsd.workshop.migration;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.data.MigDataJdbcRepository;
import com.tsd.workshop.migration.data.MigDataRepository;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
public class MigDataService {
    @Autowired
    private MigDataRepository migDataRepository;

    @Autowired
    private MigDataJdbcRepository migDataJdbcRepository;

    @Autowired
    private SparePartUsageService sparePartUsageService;

    public Mono<MigData> findById(Long id) {
        return migDataRepository.findById(id);
    }

    public Flux<MigData> findAll() {
        return migDataRepository.findAll(Sort.by(
                Sort.Order.desc("completionDate").nullsFirst(),
                Sort.Order.desc("creationDate")));
    }

    @Transactional
    public Mono<Long> deleteById(Long id) {
        // remove from spare_part_usage too
        return migDataJdbcRepository.moveToDeletedTable(id)
                .flatMap(count ->
                    sparePartUsageService.deleteByServiceId(id)
                            .thenReturn(count)
                );
    }

    @Transactional
    public Flux<MigData> saveAll(Collection<MigData> transactions) {
        // create spare_part_usage
        return migDataRepository.saveAll(transactions)
                .collectList()
                .flatMapMany(mds ->
                    sparePartUsageService.saveAllSparePartUsages(mds.stream().map(md -> {
                        SparePartUsage spu = new SparePartUsage();
                        spu.setQuantity(md.getQuantity());
                        spu.setServiceId(md.getIndex());
                        spu.setVehicleNo(md.getVehicleNo());
                        spu.setOrderId(md.getSupplierId());
                        spu.setUsageDate(md.getCreationDate());
                        return spu;
                    }).toList()).thenMany(Flux.fromIterable(mds))
                );
    }
}
