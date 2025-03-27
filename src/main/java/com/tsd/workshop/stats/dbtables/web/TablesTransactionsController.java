package com.tsd.workshop.stats.dbtables.web;

import com.tsd.workshop.stats.dbtables.TableTransaction;
import com.tsd.workshop.stats.dbtables.data.TablesTransactionsR2dbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stats/dbtables")
public class TablesTransactionsController {

    @Autowired
    private TablesTransactionsR2dbcRepository tablesTransactionsR2dbcRepository;

    @GetMapping
    public Flux<TableTransaction> retrieveTableTransactions() {
        return tablesTransactionsR2dbcRepository.getTablesLastTransactions();
    }
}
