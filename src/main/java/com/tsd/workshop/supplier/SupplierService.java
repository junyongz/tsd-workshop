package com.tsd.workshop.supplier;

import com.tsd.workshop.supplier.data.Supplier;
import com.tsd.workshop.supplier.data.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Mono<Supplier> saveSupplier(Supplier supplier) {
        if (supplier.getId() != null) {
            supplier.setId(null); // Force id to be null for auto-increment
        }
        return supplierRepository.save(supplier);
    }

    public Mono<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Flux<Supplier> findAll() {
        return supplierRepository.findByRecentOrdered();
    }

    public Mono<Void> deleteById(Long id) {
        return supplierRepository.deleteById(id)
                .then(Mono.empty());
    }
}