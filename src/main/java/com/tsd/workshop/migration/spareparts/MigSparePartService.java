package com.tsd.workshop.migration.spareparts;

import com.tsd.workshop.migration.spareparts.data.MigSparePart;
import com.tsd.workshop.migration.spareparts.data.MigSparePartRepository;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MigSparePartService {

    @Autowired
    private MigSparePartRepository migSparePartRepository;

    @Transactional
    public Flux<MigSparePart> smartSaveMigSpareParts(List<SupplierSparePart> supplierSpareParts) {
        return Flux.fromIterable(supplierSpareParts)
                        .flatMap(ssp ->
                                migSparePartRepository.findBySupplierIdAndOrderId(ssp.getSupplierId(), ssp.getId())
                                    .flatMap(msp -> {
                                        ssp.saveInto(msp);
                                        return migSparePartRepository.save(msp);
                                    }).switchIfEmpty(migSparePartRepository.save(ssp.toSparePart()))
                        ).flatMap(Flux::just);
    }

    public Mono<MigSparePart> saveMigSparePart(MigSparePart migSparePart) {
        return migSparePartRepository.save(migSparePart);
    }

    public Mono<MigSparePart> findById(Long id) {
        return migSparePartRepository.findById(id);
    }

    public Flux<MigSparePart> findAll() {
        return migSparePartRepository.findAllAvailableSpareParts();
    }

    public Mono<Void> deleteById(Long id) {
        return migSparePartRepository.deleteById(id);
    }

    public Mono<Void> deleteByOrderId(Long orderId) {
        return migSparePartRepository.deleteByOrderId(orderId);
    }
}
