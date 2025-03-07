package com.tsd.workshop.transaction.web;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.MigDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private MigDataService migDataService;

    @GetMapping("/{id}")
    public Mono<MigData> getSingle(@PathVariable Long id) {
        return migDataService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Long> deleteSingle(@PathVariable Long id) {
        return migDataService.deleteById(id);
    }

    @GetMapping
    public Flux<MigData> getAllTransactions() {
        return migDataService.findAll();
    }

    @PostMapping
    public Flux<MigData> saveAllTransactions(@RequestBody List<MigData> transactions,
                                             @RequestParam(name = "op", required = false) Operation op) {
        List<MigData> transactionsToSave = new ArrayList<>();

        for (MigData migData : transactions) {
            if (op == Operation.COMPLETE && !migData.isCompleted()) {
                migData.setCompletionDate(LocalDate.now());
            }
            transactionsToSave.add(migData);
        }

        return migDataService.saveAll(transactionsToSave);
    }
}
