package com.tsd.workshop.transaction.web;

import com.tsd.workshop.transaction.TransactionService;
import com.tsd.workshop.transaction.TransactionType;
import com.tsd.workshop.transaction.VehicleOngoingServiceException;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/workshop-services")
public class WorkshopServiceController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SparePartUsageService sparePartUsageService;

    @GetMapping("/{id}")
    public Mono<WorkshopService> getSingle(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Long> deleteSingle(@PathVariable Long id) {
        return transactionService.deleteById(id);
    }

    @GetMapping
    public Flux<WorkshopService> getWorkshopServices(
            @RequestParam(name = "vehicleId", required = false) Long vehicleId,
            @RequestParam(name="type", required = false) TransactionType[] transactionTypes,
            @RequestParam(name="pageNumber", required = false, defaultValue = "-1") int pageNum,
            @RequestParam(name="pageSize", required = false, defaultValue = "-1") int pageSize,
            @RequestParam(name="year", required = false, defaultValue = "-1") int year,
            @RequestParam(name="month", required = false, defaultValue = "-1") int month,
            @RequestParam(name="keyword", required = false) List<String> keywords
    ) {
        if (vehicleId != null) {
            return transactionService.findByVehicleId(vehicleId);
        }
        if (transactionTypes != null) {
            return transactionService.findLatestByTransactionTypes(transactionTypes);
        }
        if (pageNum >= 0 && pageSize >= 0) {
            return transactionService.findWithPages(pageNum, pageSize);
        }
        if (year > 0 && month > 0) {
            return transactionService.findByYearAndMonth(year, month);
        }
        if (keywords != null && !keywords.isEmpty()) {
            return transactionService.searchByKeywords(keywords);
        }

        return transactionService.findAll();
    }

    @PostMapping
    public Mono<WorkshopService> saveWorkshopService(@RequestBody WorkshopService workshopService,
                                                     @RequestParam(name = "op", required = false) Operation op) {
        if (op == Operation.COMPLETE) {
            if (workshopService.getCompletionDate() == null) {
                workshopService.setCompletionDate(LocalDate.now());
            }
            return transactionService.completeService(workshopService);
        }

        if (op == Operation.NOTE) {
            return transactionService.updateNote(workshopService);
        }

        Mono<WorkshopService> updateRoutine = transactionService.findByVehicleId(workshopService.getVehicleId())
                .collectList()
                .flatMap(wss -> {
                    if (!wss.isEmpty() && !Objects.equals(wss.getFirst().getId(), workshopService.getId())) {
                        throw new VehicleOngoingServiceException(wss.getFirst().getVehicleNo(), wss.getFirst().getStartDate());
                    }
                    return Mono.empty();
                })
                .then(transactionService.save(workshopService));

        List<SparePartUsage> sparePartUsages = workshopService.getSparePartUsages();
        if (sparePartUsages != null && !sparePartUsages.isEmpty()) {
            return sparePartUsageService.validateSparePartUsageByQuantity(sparePartUsages)
                    .all(Boolean.TRUE::equals)
                    .flatMap(truly -> updateRoutine);
        }

        return updateRoutine;
    }

}
