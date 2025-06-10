package com.tsd.workshop.transaction;

import com.tsd.workshop.migration.data.MigDataRepository;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.data.WorkshopServiceRepository;
import com.tsd.workshop.transaction.data.WorkshopServiceSqlRepository;
import com.tsd.workshop.transaction.media.WorkshopServiceMediaService;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import com.tsd.workshop.workmanship.WrongWorkmanshipOwnerException;
import com.tsd.workshop.workmanship.data.WorkmanshipTask;
import com.tsd.workshop.workmanship.data.WorkmanshipTaskRepository;
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
    private WorkmanshipTaskRepository workmanshipTaskRepository;

    @Autowired
    private WorkshopServiceSqlRepository workshopServiceSqlRepository;

    @Autowired
    private WorkshopServiceMediaService workshopServiceMediaService;

    public Mono<WorkshopService> findById(Long id) {
        return this.workshopServiceRepository.findById(id)
                .flatMap(ws ->
                Mono.zip(
                    migDataRepository.findByServiceId(ws.getId()).collectList(),
                    sparePartUsageRepository.findByServiceId(ws.getId()).collectList(),
                    workshopServiceMediaService.countByServiceId(ws.getId()),
                    workmanshipTaskRepository.findByServiceId(ws.getId()).collectList()
                ).map(t -> {
                    ws.setMigratedHandWrittenSpareParts(t.getT1());
                    ws.setSparePartUsages(t.getT2());
                    ws.setTasks(t.getT4());
                    ws.setUploadedMediasCount(t.getT3());
                    ws.setSparePartsCount(t.getT2().size());
                    ws.setWorkmanshipTasksCount(t.getT4().size());
                    return ws;
                }));
    }

    @Transactional
    public Mono<Long> deleteById(Long id) {
        return this.workshopServiceSqlRepository.moveToDeletedTable(id)
                .flatMap( count -> this.workshopServiceRepository.deleteById(id))
                .then(sparePartUsageRepository.deleteByServiceId(id))
                .then(workmanshipTaskRepository.deleteByServiceId(id))
                .then(Mono.just(id));
    }

    public Flux<WorkshopService> findAll() {
        return this.workshopServiceMediaService.groupedServiceIdCounts()
                .flatMapMany(countByServiceId ->
                    this.workshopServiceRepository.findAll(Sort.by(
                                    Sort.Order.desc("completionDate").nullsFirst(),
                                    Sort.Order.desc("startDate")))
                            .flatMapSequential(ws -> {
                                if (ws.getCompletionDate() == null) {
                                    return Flux.zip(
                                        migDataRepository.findByServiceId(ws.getId()).collectList(),
                                        sparePartUsageRepository.findByServiceId(ws.getId()).collectList(),
                                        workmanshipTaskRepository.findByServiceId(ws.getId()).collectList()
                                    ).map(t -> {
                                        ws.setMigratedHandWrittenSpareParts(t.getT1());
                                        ws.setSparePartUsages(t.getT2());
                                        ws.setTasks(t.getT3());
                                        ws.setUploadedMediasCount(countByServiceId.getOrDefault(ws.getId(), 0));
                                        ws.setSparePartsCount(t.getT2().size());
                                        ws.setWorkmanshipTasksCount(t.getT3().size());
                                        return ws;
                                    });
                                }

                                ws.setUploadedMediasCount(countByServiceId.getOrDefault(ws.getId(), 0));
                                return Mono.just(ws);
                            })
                );
    }

    public Flux<WorkshopService> findWithPages(int pageNum, int pageSize) {
        return populateAssociations(this.workshopServiceRepository.findAllBy(PageRequest.of(pageNum, pageSize)
                        .withSort(Sort.by(
                                Sort.Order.desc("completionDate").nullsFirst(),
                                Sort.Order.desc("startDate")))));
    }

    public Flux<WorkshopService> findByYearAndMonth(int year, int month) {
        return populateAssociations(this.workshopServiceRepository.findByYearAndMonth(year, month));
    }

    @Transactional
    public Mono<WorkshopService> save(WorkshopService workshopService) {
        return workshopServiceRepository.save(workshopService)
                .flatMap(ws -> {
                    for (SparePartUsage spu : workshopService.getSparePartUsages()) {
                        spu.setServiceId(ws.getId());
                    }
                    for (WorkmanshipTask task : workshopService.getTasks()) {
                        task.setServiceId(ws.getId());
                    }

                    return sparePartUsageRepository.saveAll(workshopService.getSparePartUsages())
                            .all(spu -> spu.getId() != null)
                            .map(b -> ws)
                            .flatMapMany(wss -> workmanshipTaskRepository.saveAll(workshopService.getTasks()))
                            .all(task -> task.getId() != null)
                            .map( b -> ws);
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

    @Transactional
    public Mono<WorkshopService> updateNote(WorkshopService ws) {
        return workshopServiceSqlRepository.updateNoteFor(ws);
    }

    public Flux<WorkshopService> findByState(State state) {
        if (state == State.PENDING) {
            return workshopServiceRepository.findByCompletionDateIsNull();
        }
        else if (state == State.DONE) {
            return workshopServiceRepository.findByCompletionDateIsNotNull();
        }
        return Flux.empty();
    }

    public Flux<WorkshopService> searchByKeywords(List<String> keywords) {
        return populateAssociations(workshopServiceSqlRepository.searchServiceIdsByKeywords(keywords)
                .flatMap(workshopServiceRepository::findById));
    }

    @Transactional
    public Mono<Long> deleteTask(Long serviceId, Long taskId) {
        return workmanshipTaskRepository.findById(taskId)
                .flatMap(task -> {
                    if (!serviceId.equals(task.getServiceId())) {
                        throw new WrongWorkmanshipOwnerException(task, serviceId);
                    }
                    return workmanshipTaskRepository.deleteById(taskId).then(Mono.just(taskId));
                });
    }

    private Flux<WorkshopService> populateAssociations(Flux<WorkshopService> wss) {
        return wss.flatMapSequential(ws ->
                Flux.zip(
                    migDataRepository.findByServiceId(ws.getId()).collectList(),
                    sparePartUsageRepository.findByServiceId(ws.getId()).collectList(),
                    workmanshipTaskRepository.findByServiceId(ws.getId()).collectList(),
                    workshopServiceMediaService.countByServiceId(ws.getId())
                ).map(t -> {
                    ws.setMigratedHandWrittenSpareParts(t.getT1());
                    ws.setSparePartUsages(t.getT2());
                    ws.setTasks(t.getT3());
                    ws.setUploadedMediasCount(t.getT4());
                    ws.setSparePartsCount(t.getT2().size());
                    ws.setWorkmanshipTasksCount(t.getT3().size());
                    return ws;
                })
        );
    }

}
