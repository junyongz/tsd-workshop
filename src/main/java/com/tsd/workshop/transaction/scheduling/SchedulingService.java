package com.tsd.workshop.transaction.scheduling;

import com.tsd.workshop.transaction.scheduling.data.ScheduledService;
import com.tsd.workshop.transaction.scheduling.data.ScheduledServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class SchedulingService {

    @Autowired
    private ScheduledServiceRepository scheduledServiceRepository;

    public Flux<ScheduledService> getAll(LocalDate localDate) {
        return this.scheduledServiceRepository.findByScheduledDateAfter(localDate);
    }

    public Mono<Void> deleteById(Long id) {
        return this.scheduledServiceRepository.deleteById(id);
    }

    public Mono<ScheduledService> createNew(ScheduledService service) {
        return this.scheduledServiceRepository.save(service);
    }
}
