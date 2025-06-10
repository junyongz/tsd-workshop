package com.tsd.workshop.transaction.web;

import com.tsd.workshop.transaction.TransactionService;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.media.WorkshopServiceMediaService;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WorkshopServiceControllerTest {

    @Test
    void completeService() {
        TransactionService transactionService = mock(TransactionService.class);
        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        WorkshopServiceMediaService workshopServiceMediaService = mock(WorkshopServiceMediaService.class);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(WorkshopServiceController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("transactionService", transactionService);
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService);
        ctx.getDefaultListableBeanFactory().registerSingleton("workshopServiceMediaService", workshopServiceMediaService);
        ctx.refresh();

        WorkshopService ws = new WorkshopService();
        ws.setId(2000L);
        ws.setCompletionDate(LocalDate.of(2021, 12, 23));

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/api/workshop-services?op=COMPLETE")
                .body(BodyInserters.fromValue(ws))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        ArgumentCaptor<WorkshopService> wsCaptor = ArgumentCaptor.forClass(WorkshopService.class);
        verify(transactionService).completeService(wsCaptor.capture());
        assertThat(wsCaptor.getValue().getId()).isEqualTo(2000L);
        assertThat(wsCaptor.getValue().getCompletionDate()).isEqualTo(LocalDate.of(2021, 12, 23));
    }

    @Test
    void completeServiceWithoutDate() {
        TransactionService transactionService = mock(TransactionService.class);
        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        WorkshopServiceMediaService workshopServiceMediaService = mock(WorkshopServiceMediaService.class);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(WorkshopServiceController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("transactionService", transactionService);
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService);
        ctx.getDefaultListableBeanFactory().registerSingleton("workshopServiceMediaService", workshopServiceMediaService);
        ctx.refresh();

        WorkshopService ws = new WorkshopService();
        ws.setId(2000L);

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/api/workshop-services?op=COMPLETE")
                .body(BodyInserters.fromValue(ws))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

        ArgumentCaptor<WorkshopService> wsCaptor = ArgumentCaptor.forClass(WorkshopService.class);
        verify(transactionService).completeService(wsCaptor.capture());
        assertThat(wsCaptor.getValue().getId()).isEqualTo(2000L);
        // this probably would failed in split nanosecond between running until verification over here
        assertThat(wsCaptor.getValue().getCompletionDate()).isEqualTo(LocalDate.now());
    }
}
