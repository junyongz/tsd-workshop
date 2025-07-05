package com.tsd.workshop.sparepart.web;

import com.tsd.workshop.sparepart.SparePartService;
import com.tsd.workshop.sparepart.data.SparePart;
import com.tsd.workshop.web.ResponsePaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/spare-parts")
public class SparePartController {

    @Autowired
    private SparePartService sparePartService;

    @PostMapping
    public Mono<SparePart> saveSpartPart(@RequestBody SparePart sparePart) {
        sparePart.setCreationDate(LocalDate.now());
        return sparePartService.saveSparePart(sparePart);
    }

    @GetMapping("/{id}")
    public Mono<SparePart> getSparePart(@PathVariable Long id) {
        return sparePartService.findById(id);
    }

    @GetMapping
    public Flux<SparePart> getAllSpareParts(
            @RequestParam(name="pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize", required = false, defaultValue = "-1") int pageSize,
            ServerHttpResponse response
    ) {
        if (pageNumber > 0 && pageSize >= 0) {
            return sparePartService.totalSpareParts()
                    .flatMapMany(count -> {
                        ResponsePaginationUtils.setHeaders(response.getHeaders(), count, pageNumber, pageSize);
                        return sparePartService.findAll(pageNumber, pageSize);
            });
        }
        return sparePartService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Long> deleteSparePart(@PathVariable Long id) {
        return sparePartService.deleteById(id).thenReturn(id);
    }

}
