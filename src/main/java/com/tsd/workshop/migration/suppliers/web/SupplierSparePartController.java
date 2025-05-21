package com.tsd.workshop.migration.suppliers.web;

import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.migration.suppliers.SupplierSparePartService;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
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

    @Autowired
    private SparePartUsageService sparePartUsageService;

    /**
     * if there is spare part usage for the same order id, original quantity (from db) must be same as the provided quantity
     *
     * @param supplierSpareParts spare parts recorded for the supplier to be saved
     * @return the saved one
     */
    @PostMapping
    public Flux<SupplierSparePart> saveSupplierSpareParts(@RequestBody List<SupplierSparePart> supplierSpareParts,
                                                            @RequestParam(value = "op", required = false) Operation operation) {
        if (operation == Operation.NOTES) {
            // only will deal with the first supplier spare parts
            return supplierSparePartService.updateNotes(supplierSpareParts.getFirst()).flux();
        }

        List<SupplierSparePart> editingSsp = supplierSpareParts.stream().filter(ssp -> ssp.getId() != null)
                .toList();

        // TAKE NOTE of <code>all</code> replace it with <code>flatMap</code> would cause more data created
        return sparePartUsageService.validateSparePartUsageQuantityForEditing(editingSsp)
                .all(Boolean.TRUE::equals)
                .flatMapMany(res -> supplierSparePartService.saveSupplierSpareParts(supplierSpareParts));
    }

    @GetMapping("/{id}")
    public Mono<SupplierSparePart> getSupplierSparePart(@PathVariable Long id) {
        return supplierSparePartService.findById(id);
    }

    @GetMapping
    public Flux<SupplierSparePart> getAllSupplierSpareParts(@RequestParam(value = "fetch", defaultValue = "ACTIVE") Fetch fetchMode) {
        if (fetchMode == Fetch.ACTIVE) {
            return supplierSparePartService.findWithUsage();
        }

        return supplierSparePartService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSupplierSparePart(@PathVariable Long id) {
        return supplierSparePartService.deleteById(id)
                .then(Mono.empty());
    }
}
