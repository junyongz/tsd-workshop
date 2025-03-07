package com.tsd.workshop.migration.spareparts;

import com.tsd.workshop.migration.spareparts.data.MigSparePart;
import com.tsd.workshop.migration.spareparts.data.MigSparePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MigSparePartService {

    @Autowired
    private MigSparePartRepository migSparePartRepository;

    public Mono<MigSparePart> saveMigSparePart(MigSparePart migSparePart) {
        return migSparePartRepository.save(migSparePart);
    }

    public Mono<MigSparePart> findById(Long id) {
        return migSparePartRepository.findById(id);
    }

    public Flux<MigSparePart> findAll() {
        return migSparePartRepository.findAll();
    }

    public Mono<Void> deleteById(Long id) {
        return migSparePartRepository.deleteById(id);
    }
}
