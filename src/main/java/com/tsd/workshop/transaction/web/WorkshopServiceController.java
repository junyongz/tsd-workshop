package com.tsd.workshop.transaction.web;

import com.tsd.workshop.migration.MigDataService;
import com.tsd.workshop.transaction.TransactionService;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/workshop-services")
public class WorkshopServiceController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MigDataService migDataService;

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
    public Flux<WorkshopService> getWorkshopServices(@RequestParam(name = "vehicleId", required = false) Long vehicleId) {
        if (vehicleId != null) {
            return transactionService.findByVehicleId(vehicleId);
        }
        return transactionService.findAll();
    }

    @PostMapping
    public Mono<WorkshopService> saveWorkshopService(@RequestBody WorkshopService workshopService,
                                                     @RequestParam(name = "op", required = false) Operation op) {
        if (op == Operation.COMPLETE) {
            workshopService.setCompletionDate(LocalDate.now());
        }
        return transactionService.save(workshopService);
    }
}
