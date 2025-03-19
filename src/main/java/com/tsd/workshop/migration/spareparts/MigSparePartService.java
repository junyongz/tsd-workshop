package com.tsd.workshop.migration.spareparts;

import com.tsd.workshop.migration.spareparts.data.MigSparePart;
import com.tsd.workshop.migration.spareparts.data.MigSparePartRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MigSparePartService {

    @Autowired
    private MigSparePartRepository migSparePartRepository;

    public Flux<MigSparePart> saveMigSparePartsForNewOrder(List<SupplierSparePart> supplierSpareParts) {
        return migSparePartRepository.saveAll(
                supplierSpareParts.stream()
                        .map(SupplierSparePart::toSparePart)
                        .collect(Collectors.toList())
        );
    }

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
