package com.tsd.workshop.migration;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.data.MigDataJdbcRepository;
import com.tsd.workshop.migration.data.MigDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
public class MigDataService {
    @Autowired
    private MigDataRepository migDataRepository;

    @Autowired
    private MigDataJdbcRepository migDataJdbcRepository;

    public Mono<MigData> findById(Long id) {
        return migDataRepository.findById(id);
    }

    public Flux<MigData> findAll() {
        return migDataRepository.findAll(Sort.by(
                Sort.Order.desc("completionDate").nullsFirst(),
                Sort.Order.desc("creationDate")));
    }

    public Mono<Long> deleteById(Long id) {
        return migDataJdbcRepository.moveToDeletedTable(id);
    }

    public Flux<MigData> saveAll(Collection<MigData> transactions) {
        return migDataRepository.saveAll(transactions);
    }
}
