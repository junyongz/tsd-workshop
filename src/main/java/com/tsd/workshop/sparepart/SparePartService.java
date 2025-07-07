package com.tsd.workshop.sparepart;

import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import com.tsd.workshop.sparepart.data.SparePart;
import com.tsd.workshop.sparepart.data.SparePartRepository;
import com.tsd.workshop.sparepart.data.SparePartSqlRepository;
import com.tsd.workshop.sparepart.media.data.SparePartMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SparePartService {

    private final Sort defaultSort = Sort.by(Sort.Order.desc("creationDate"),
                Sort.Order.desc("id"));
    
    @Autowired
    private SparePartRepository sparePartRepository;

    @Autowired
    private SparePartSqlRepository sparePartSqlRepository;

    @Autowired
    private SparePartMediaRepository sparePartMediaRepository;

    @Autowired
    private SupplierSparePartR2dbcRepository supplierSparePartR2dbcRepository;

    @Transactional
    public Mono<SparePart> saveSparePart(SparePart sparePart) {
        return sparePartRepository.save(sparePart)
                .flatMap(sp ->
                        sparePart.getOrderIds() != null && !sparePart.getOrderIds().isEmpty() ? supplierSparePartR2dbcRepository.updateWithSparePartId(sp.getId(), sp.getOrderIds())
                            .then(Mono.just(sp)) : Mono.just(sp)
                )
                .flatMap(sp -> sparePartRepository.findById(sp.getId()));
    }

    public Mono<SparePart> findById(Long id) {
        return sparePartRepository.findById(id);
    }

    public Flux<SparePart> findAll() {
        return sparePartRepository.findAll(defaultSort);
    }

    public Flux<SparePart> findAll(List<String> keywords, int pageNumber, int pageSize) {
        return sparePartSqlRepository.searchByKeywords(keywords, pageNumber, pageSize)
                .flatMap(id -> sparePartRepository.findById(id));
    }

    public Flux<SparePart> findAll(int pageNumber, int pageSize) {
        return sparePartRepository.findAllBy(PageRequest.of(pageNumber-1, pageSize, defaultSort));
    }

    public Mono<Long> totalSpareParts() {
        return sparePartRepository.count();
    }

    public Mono<Long> totalSpareParts(List<String> keywords) {
        return sparePartSqlRepository.countByKeywords(keywords);
    }

    @Transactional
    public Mono<Void> deleteById(Long id) {
        return supplierSparePartR2dbcRepository.updateSparePartIdToNull(id)
                        .then(sparePartRepository.deleteById(id))
                        .then(sparePartMediaRepository.deleteBySparePartId(id));
    }

}
