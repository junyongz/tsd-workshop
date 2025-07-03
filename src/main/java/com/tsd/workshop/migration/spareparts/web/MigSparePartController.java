package com.tsd.workshop.migration.spareparts.web;

import com.tsd.workshop.migration.spareparts.MigSparePartService;
import com.tsd.workshop.migration.spareparts.data.MigSparePart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/mig-spare-parts")
public class MigSparePartController {
    @Autowired
    private MigSparePartService migSparePartService;

    @PostMapping
    public Mono<MigSparePart> createMigSparePart(@RequestBody MigSparePart migSparePart) {
        return migSparePartService.saveMigSparePart(migSparePart);
    }

    @GetMapping("/{id}")
    public Mono<MigSparePart> getMigSparePart(@PathVariable Long id) {
        return migSparePartService.findById(id);
    }

    @GetMapping
    public Flux<MigSparePart> getAllMigSpareParts() {
        // TODO: not to give remaining is 0 one
        return migSparePartService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMigSparePart(@PathVariable Long id) {
        return migSparePartService.deleteById(id);
    }
}
