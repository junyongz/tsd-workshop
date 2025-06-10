package com.tsd.workshop.transaction.utilization.web;

import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spare-part-utilizations")
public class SparePartUsageController {
    @Autowired
    private SparePartUsageService sparePartUsageService;

    @GetMapping
    public Flux<SparePartUsage> getAllSparePartUsages() {
        return sparePartUsageService.findAll();
    }

    @PostMapping
    public Mono<SparePartUsage> createSparePartUsage(@RequestBody SparePartUsage sparePartUsage) {
        if (sparePartUsage.getOrderId() == null) {
            throw new IllegalArgumentException("spare part usage must have orderId");
        }
        return sparePartUsageService.saveSparePartUsage(sparePartUsage);
    }

    @DeleteMapping("/{sparePartUsageId}")
    public Mono<Long> deleteSparePart(@PathVariable Long sparePartUsageId) {
        return sparePartUsageService.deleteById(sparePartUsageId);
    }

}
