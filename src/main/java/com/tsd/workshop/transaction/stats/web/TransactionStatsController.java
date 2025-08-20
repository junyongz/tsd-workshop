package com.tsd.workshop.transaction.stats.web;

import com.tsd.workshop.transaction.stats.TransactionStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction-stats")
public class TransactionStatsController {

    @Autowired
    private TransactionStatisticsService transactionStatisticsService;

    @GetMapping
    public Flux<Map<String, Object>> getByDateRange(@RequestParam("fromDate") LocalDate fromDate, @RequestParam("toDate") LocalDate toDate) {
        return transactionStatisticsService.queryByStartDateRange(fromDate, toDate);
    }
}
