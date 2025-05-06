package com.tsd.workshop.transaction.web;

import com.tsd.workshop.migration.MigDataService;
import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @SuppressWarnings("unchecked")
    @Test
    void saveAllTransactions() {
        MigDataService migDataService = mock(MigDataService.class);
        ArgumentCaptor<List<MigData>> migDatasArgCaptor = ArgumentCaptor.forClass(List.class);
        when(migDataService.saveAll(migDatasArgCaptor.capture())).then(args -> {
            List<MigData> migDatas = args.getArgument(0);
            migDatas.get(0).setIndex(1000L);
            migDatas.get(1).setIndex(1001L);
            return Flux.just(migDatas.get(0), migDatas.get(1));
        });

        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        when(sparePartUsageService.validateSparePartUsageByQuantity(migDatasArgCaptor.capture())).thenReturn(Flux.just(true, true));

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(TransactionController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("migDataService", migDataService );
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService );
        ctx.refresh();

        MigData md1 = new MigData();
        md1.setItemDescription("Spare part 1");
        md1.setQuantity(BigDecimal.ONE);
        md1.setUnit("pc");
        md1.setUnitPrice(new BigDecimal("30.50"));
        md1.setTotalPrice(new BigDecimal("30.50"));

        MigData md2 = new MigData();
        md2.setItemDescription("Spare part 2");
        md2.setQuantity(new BigDecimal(20));
        md2.setUnit("litre");
        md2.setUnitPrice(new BigDecimal("10.00"));
        md2.setTotalPrice(new BigDecimal("200.00"));

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/transactions")
                .body(BodyInserters.fromValue(List.of(md1, md2)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        [{"index":1000,"sheetName":null,"vehicleNo":null,"creationDate":null,
                        "itemDescription":"Spare part 1","quantity":1,"unit":"pc","unitPrice":30.50,"totalPrice":30.50,
                        "calculatedTotalPrice":null,"migratedIndicator":false,"completionDate":null},
                        {"index":1001,"sheetName":null,"vehicleNo":null,"creationDate":null,
                        "itemDescription":"Spare part 2","quantity":20,"unit":"litre","unitPrice":10.00,"totalPrice":200.00,
                        "calculatedTotalPrice":null,"migratedIndicator":false,"completionDate":null}]
                        """);

        ctx.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    void completeTransaction() {
        MigDataService migDataService = mock(MigDataService.class);
        ArgumentCaptor<List<MigData>> migDatasArgCaptor = ArgumentCaptor.forClass(List.class);
        when(migDataService.saveAll(migDatasArgCaptor.capture())).then(args -> {
            List<MigData> migDatas = args.getArgument(0);
            migDatas.get(0).setIndex(1000L);
            migDatas.get(1).setIndex(1001L);
            return Flux.just(migDatas.get(0), migDatas.get(1));
        });

        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        when(sparePartUsageService.validateSparePartUsageByQuantity(migDatasArgCaptor.capture())).thenReturn(Flux.just(true, true));

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(TransactionController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("migDataService", migDataService );
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService );
        ctx.refresh();

        MigData md1 = new MigData();
        md1.setItemDescription("Spare part 1");
        md1.setQuantity(BigDecimal.ONE);
        md1.setUnit("pc");
        md1.setUnitPrice(new BigDecimal("30.50"));
        md1.setTotalPrice(new BigDecimal("30.50"));

        MigData md2 = new MigData();
        md2.setItemDescription("Spare part 2");
        md2.setQuantity(new BigDecimal(20));
        md2.setUnit("litre");
        md2.setUnitPrice(new BigDecimal("10.00"));
        md2.setTotalPrice(new BigDecimal("200.00"));

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/transactions?op=COMPLETE")
                .body(BodyInserters.fromValue(List.of(md1, md2)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        List<MigData> migDatas = migDatasArgCaptor.getValue();
        assertThat(migDatas.get(0).getCompletionDate()).isNotNull();
        assertThat(migDatas.get(1).getCompletionDate()).isNotNull();

        ctx.close();
    }

    // TODO add where validate failed
}
