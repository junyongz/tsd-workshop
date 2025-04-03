package com.tsd.workshop.migration.suppliers.web;

import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.SupplierSparePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/supplier-spare-parts")
public class SupplierSparePartController {
    @Autowired
    private SupplierSparePartService supplierSparePartService;

    // TODO validate any quantity changed after use, not suppose to anymore
    @PostMapping
    public Flux<SupplierSparePart> createSupplierSpareParts(@RequestBody List<SupplierSparePart> supplierSparePart) {
        return supplierSparePartService.saveSupplierSpareParts(supplierSparePart);
    }

    @GetMapping("/{id}")
    public Mono<SupplierSparePart> getSupplierSparePart(@PathVariable Long id) {
        return supplierSparePartService.findById(id);
    }

    @GetMapping
    public Flux<SupplierSparePart> getAllSupplierSpareParts() {
        return supplierSparePartService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSupplierSparePart(@PathVariable Long id) {
        return supplierSparePartService.deleteById(id)
                .then(Mono.empty());
    }
}
