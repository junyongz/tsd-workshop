package com.tsd.workshop.transaction.stats;

import com.tsd.workshop.transaction.stats.data.TransactionStatsSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.Map;

@Service
public class TransactionStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionStatisticsService.class);

    @Autowired
    private TransactionStatsSqlRepository transactionStatsSqlRepository;

    public Flux<Map<String, Object>> queryByStartDateRange(LocalDate fromDate, LocalDate toDate) {
        return transactionStatsSqlRepository.queryByStartDateRange(fromDate, toDate);
    }

    @Scheduled(cron = "${transaction.statistics.collection.cron}")
    public void pollingDailyTransactionStats() {
        transactionStatsSqlRepository
                .cleanInstall()
                .flatMap(count -> {
                    logger.info("total number of records {} inserted", count);
                    return Mono.just(count);
                })
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

}
