package com.tsd.workshop.transaction.scheduling.web;

import com.tsd.workshop.transaction.scheduling.SchedulingService;
import com.tsd.workshop.transaction.scheduling.data.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingServiceController {

    @Autowired
    private SchedulingService schedulingService;

    @GetMapping
    public Flux<ScheduledService> getAll() {
        return schedulingService.getAll(LocalDate.now().minusDays(1));
    }

    @PostMapping
    public Mono<ScheduledService> createNew(@RequestBody ScheduledService service) {
        return schedulingService.createNew(service);
    }

    @DeleteMapping("/{id}")
    public Mono<Long> createNew(@PathVariable("id") Long id) {
        return schedulingService.deleteById(id)
                .then(Mono.just(id));
    }

}
