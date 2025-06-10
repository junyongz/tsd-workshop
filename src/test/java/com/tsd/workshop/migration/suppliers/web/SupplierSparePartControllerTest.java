package com.tsd.workshop.migration.suppliers.web;

import com.tsd.workshop.migration.suppliers.SupplierSparePartService;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.transaction.utilization.SparePartUsageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SupplierSparePartControllerTest {

    @SuppressWarnings("unchecked")
    @Test
    void saveWithBrandNew() {
        ArgumentCaptor<List<SupplierSparePart>> sspEditingCaptor = ArgumentCaptor.forClass(List.class);
        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        when(sparePartUsageService.validateSparePartUsageQuantityForEditing(sspEditingCaptor.capture())).thenReturn(Flux.just(true));

        ArgumentCaptor<List<SupplierSparePart>> sspCaptor = ArgumentCaptor.forClass(List.class);
        SupplierSparePartService supplierSparePartService = mock(SupplierSparePartService.class);
        SupplierSparePart ssp1 = new SupplierSparePart();
        ssp1.setId(1000L);
        ssp1.setPartName("Iron Rod 20x70");
        when(supplierSparePartService.saveSupplierSpareParts(sspCaptor.capture())).thenReturn(Flux.just(ssp1));

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(SupplierSparePartController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("supplierSparePartService", supplierSparePartService);
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService);
        ctx.refresh();

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/api/supplier-spare-parts")
                .body(BodyInserters.fromValue("""
                        [{"id":20667,"deliveryOrderNo":"7776662","computedDate":null,"invoiceDate":"2025-04-03",
                        "itemCode":"WG-2203210410","partName":"WG-2203210410 CYLINDER PUMP , GEAR SHIFT","particular":null,
                        "quantity":3,"unit":"pcs","unitPrice":1067,"notes":null,"supplierId":18585,"sheetName":null}]
                        """))
                .headers(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        [{"id": 1000,"deliveryOrderNo":null,"computedDate":null,"invoiceDate":null,"itemCode":null,
                        "partName":"Iron Rod 20x70","particular":null,"quantity":null,"unit":null,"unitPrice":null,
                        "notes":null,"supplierId":null,"sheetName":null}]
                        """);

        ctx.close();

        assertThat(sspCaptor.getValue().getFirst().getDeliveryOrderNo()).isEqualTo("7776662");
    }

    @SuppressWarnings("unchecked")
    @Test
    void saveWithExistingButAllNotUsed() {
        ArgumentCaptor<List<SupplierSparePart>> sspEditingCaptor = ArgumentCaptor.forClass(List.class);
        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        when(sparePartUsageService.validateSparePartUsageQuantityForEditing(sspEditingCaptor.capture())).thenReturn(Flux.just(true, true));

        ArgumentCaptor<List<SupplierSparePart>> sspCaptor = ArgumentCaptor.forClass(List.class);
        SupplierSparePartService supplierSparePartService = mock(SupplierSparePartService.class);
        SupplierSparePart ssp1 = new SupplierSparePart();
        ssp1.setId(1000L);
        ssp1.setPartName("Iron Rod 20x70");
        SupplierSparePart ssp2= new SupplierSparePart();
        ssp2.setId(1001L);
        ssp2.setPartName("Clutch Disc");
        when(supplierSparePartService.saveSupplierSpareParts(sspCaptor.capture())).thenReturn(Flux.just(ssp1, ssp2));

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(SupplierSparePartController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("supplierSparePartService", supplierSparePartService);
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService);
        ctx.refresh();

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/api/supplier-spare-parts")
                .body(BodyInserters.fromValue("""
                        [{"id":20667,"deliveryOrderNo":"7776662","computedDate":null,"invoiceDate":"2025-04-03",
                        "itemCode":"WG-2203210410","partName":"WG-2203210410 CYLINDER PUMP , GEAR SHIFT","particular":null,
                        "quantity":3,"unit":"pcs","unitPrice":1067,"notes":null,"supplierId":18585,"sheetName":null},
                        {"id":20668,"deliveryOrderNo":"7776662","computedDate":null,"invoiceDate":"2025-04-03",
                        "itemCode":"WG-2203210410","partName":"WG-2203210410 CYLINDER PUMP , GEAR SHIFT","particular":null,
                        "quantity":3,"unit":"pcs","unitPrice":1067,"notes":null,"supplierId":18585,"sheetName":null}]
                        """))
                .headers(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        [{"id": 1000,"deliveryOrderNo":null,"computedDate":null,"invoiceDate":null,"itemCode":null,
                        "partName":"Iron Rod 20x70","particular":null,"quantity":null,"unit":null,"unitPrice":null,
                        "notes":null,"supplierId":null,"sheetName":null},
                        {"id": 1001,"deliveryOrderNo":null,"computedDate":null,"invoiceDate":null,"itemCode":null,
                        "partName":"Clutch Disc","particular":null,"quantity":null,"unit":null,"unitPrice":null,
                        "notes":null,"supplierId":null,"sheetName":null}]
                        """);

        ctx.close();

        assertThat(sspCaptor.getValue().getFirst().getDeliveryOrderNo()).isEqualTo("7776662");
        assertThat(sspCaptor.getValue().getLast().getDeliveryOrderNo()).isEqualTo("7776662");
    }

    @SuppressWarnings("unchecked")
    @Test
    void saveWithExistingAndOneIsUsed() {
        ArgumentCaptor<List<SupplierSparePart>> sspEditingCaptor = ArgumentCaptor.forClass(List.class);
        SparePartUsageService sparePartUsageService = mock(SparePartUsageService.class);
        when(sparePartUsageService.validateSparePartUsageQuantityForEditing(sspEditingCaptor.capture())).thenReturn(
                Flux.create(emitter -> {
                    emitter.next(true);
                    emitter.error(new IllegalArgumentException("failed"));
                }));

        SupplierSparePartService supplierSparePartService = mock(SupplierSparePartService.class);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DelegatingWebFluxConfiguration.class);
        ctx.register(SupplierSparePartController.class);
        ctx.getDefaultListableBeanFactory().registerSingleton("supplierSparePartService", supplierSparePartService);
        ctx.getDefaultListableBeanFactory().registerSingleton("sparePartUsageService", sparePartUsageService);
        ctx.refresh();

        WebTestClient webTestClient = WebTestClient
                .bindToApplicationContext(ctx)
                .build();
        webTestClient.post().uri("/api/supplier-spare-parts")
                .body(BodyInserters.fromValue("""
                        [{"id":20667,"deliveryOrderNo":"7776662","computedDate":null,"invoiceDate":"2025-04-03",
                        "itemCode":"WG-2203210410","partName":"WG-2203210410 CYLINDER PUMP , GEAR SHIFT","particular":null,
                        "quantity":3,"unit":"pcs","unitPrice":1067,"notes":null,"supplierId":18585,"sheetName":null},
                        {"id":20668,"deliveryOrderNo":"7776662","computedDate":null,"invoiceDate":"2025-04-03",
                        "itemCode":"WG-2203210410","partName":"WG-2203210410 CYLINDER PUMP , GEAR SHIFT","particular":null,
                        "quantity":3,"unit":"pcs","unitPrice":1067,"notes":null,"supplierId":18585,"sheetName":null}]
                        """))
                .headers(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody().isEmpty();

        ctx.close();

        verifyNoInteractions(supplierSparePartService);
    }

}
