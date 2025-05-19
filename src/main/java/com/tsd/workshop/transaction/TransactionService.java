package com.tsd.workshop.transaction;

import com.tsd.workshop.migration.data.MigDataRepository;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.data.WorkshopServiceRepository;
import com.tsd.workshop.transaction.data.WorkshopServiceSqlRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private WorkshopServiceRepository workshopServiceRepository;

    @Autowired
    private MigDataRepository migDataRepository;

    @Autowired
    private SparePartUsageRepository sparePartUsageRepository;

    @Autowired
    private WorkshopServiceSqlRepository workshopServiceSqlRepository;

    public Mono<WorkshopService> findById(Long id) {
        return this.workshopServiceRepository.findById(id)
                .flatMap(ws ->
                Mono.zip(
                        migDataRepository.findByServiceId(ws.getId()).collectList(),
                        sparePartUsageRepository.findByServiceId(ws.getId()).collectList()
                ).map(t -> {
                    ws.setMigratedHandWrittenSpareParts(t.getT1());
                    ws.setSparePartUsages(t.getT2());
                    return ws;
                }));
    }

    @Transactional
    public Mono<Long> deleteById(Long id) {
        return this.workshopServiceSqlRepository.moveToDeletedTable(id)
                .flatMap( count -> this.workshopServiceRepository.deleteById(id))
                .then(sparePartUsageRepository.deleteByServiceId(id))
                .then(Mono.just(id));
    }

    public Flux<WorkshopService> findAll() {
        return this.workshopServiceRepository.findAll(Sort.by(
                    Sort.Order.desc("completionDate").nullsFirst(),
                    Sort.Order.desc("startDate")))
                .flatMapSequential(ws -> {
                    if (ws.getCompletionDate() == null) {
                        return Flux.zip(
                                migDataRepository.findByServiceId(ws.getId()).collectList(),
                                sparePartUsageRepository.findByServiceId(ws.getId()).collectList()
                        ).map(t -> {
                            ws.setMigratedHandWrittenSpareParts(t.getT1());
                            ws.setSparePartUsages(t.getT2());
                            return ws;
                        });
                    }

                    return Mono.just(ws);
                });
    }

    public Flux<WorkshopService> findWithPages(int pageNum, int pageSize) {
        return populateSpareParts(this.workshopServiceRepository.findAllBy(PageRequest.of(pageNum, pageSize)
                        .withSort(Sort.by(
                                Sort.Order.desc("completionDate").nullsFirst(),
                                Sort.Order.desc("startDate")))));
    }

    public Flux<WorkshopService> findByYearAndMonth(int year, int month) {
        return populateSpareParts(this.workshopServiceRepository.findByYearAndMonth(year, month));
    }

    @Transactional
    public Mono<WorkshopService> save(WorkshopService workshopService) {
        return workshopServiceRepository.save(workshopService)
                .flatMap(ws -> {
                    for (SparePartUsage spu : workshopService.getSparePartUsages()) {
                        spu.setServiceId(ws.getId());
                    }

                    return sparePartUsageRepository.saveAll(workshopService.getSparePartUsages())
                            .all(spu -> spu.getId() != null)
                            .map(b -> ws);
                });
    }

    public Flux<WorkshopService> findByVehicleId(Long vehicleId) {
        return workshopServiceRepository.findByVehicleIdAndCompletionDateIsNull(vehicleId);
    }

    public Flux<WorkshopService> findLatestByTransactionTypes(TransactionType[] transactionTypes) {
        return workshopServiceRepository.findLatestByTransactionTypes(transactionTypes);
    }

    @Transactional
    public Mono<WorkshopService> completeService(WorkshopService ws) {
        return workshopServiceSqlRepository.completeWorkshopService(ws);
    }

    public Flux<WorkshopService> searchByKeywords(List<String> keywords) {
        return populateSpareParts(workshopServiceSqlRepository.searchServiceIdsByKeywords(keywords)
                .flatMap(id -> workshopServiceRepository.findById(id)));
    }

    public Flux<WorkshopService> populateSpareParts(Flux<WorkshopService> wss) {
        return wss.flatMapSequential(ws ->
                Flux.zip(
                        migDataRepository.findByServiceId(ws.getId()).collectList(),
                        sparePartUsageRepository.findByServiceId(ws.getId()).collectList()
                ).map(t -> {
                    ws.setMigratedHandWrittenSpareParts(t.getT1());
                    ws.setSparePartUsages(t.getT2());
                    return ws;
                })
        );
    }
}
