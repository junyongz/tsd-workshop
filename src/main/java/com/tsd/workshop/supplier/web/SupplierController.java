package com.tsd.workshop.supplier.web;

import com.tsd.workshop.supplier.data.Supplier;
import com.tsd.workshop.supplier.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public Mono<Supplier> createSupplier(@RequestBody Supplier supplier) {
        return supplierService.saveSupplier(supplier);
    }

    @GetMapping("/{id}")
    public Mono<Supplier> getSupplier(@PathVariable Long id) {
        return supplierService.findById(id);
    }

    @GetMapping
    public Flux<Supplier> getAllSuppliers() {
        return supplierService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSupplier(@PathVariable Long id) {
        return supplierService.deleteById(id)
                .then(Mono.empty());
    }
}