package com.tsd.workshop.sparepart;

import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import com.tsd.workshop.sparepart.data.SparePart;
import com.tsd.workshop.sparepart.data.SparePartRepository;
import com.tsd.workshop.sparepart.media.data.SparePartMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SparePartService {
    
    @Autowired
    private SparePartRepository sparePartRepository;

    @Autowired
    private SparePartMediaRepository sparePartMediaRepository;

    @Autowired
    private SupplierSparePartR2dbcRepository supplierSparePartR2dbcRepository;

    @Transactional
    public Mono<SparePart> saveSparePart(SparePart sparePart) {
        return sparePartRepository.save(sparePart)
                .flatMap(sp ->
                    supplierSparePartR2dbcRepository.updateWithSparePartId(sp.getId(), sp.getOrderIds())
                            .then(Mono.just(sp))
                )
                .flatMap(sp -> sparePartRepository.findById(sp.getId()));
    }

    public Mono<SparePart> findById(Long id) {
        return sparePartRepository.findById(id);
    }

    public Flux<SparePart> findAll() {
        return sparePartRepository.findAll(Sort.by(Sort.Order.desc("creationDate")));
    }

    @Transactional
    public Mono<Void> deleteById(Long id) {
        return supplierSparePartR2dbcRepository.updateSparePartIdToNull(id)
                        .then(sparePartRepository.deleteById(id))
                        .then(sparePartMediaRepository.deleteBySparePartId(id));
    }

}
