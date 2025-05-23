package com.tsd.workshop.transaction;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.data.MigDataRepository;
import com.tsd.workshop.transaction.data.WorkshopService;
import com.tsd.workshop.transaction.data.WorkshopServiceRepository;
import com.tsd.workshop.transaction.media.WorkshopServiceMediaService;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Test
    void saveWithMoreThan1SparePartUsage() {
        WorkshopService ws = new WorkshopService();
        ws.setVehicleId(1000L);
        ws.setStartDate(LocalDate.of(2020, 12, 24));
        ws.setCreationDate(LocalDate.of(2021, 1, 1));
        ws.setCompletionDate(LocalDate.of(2021, 1, 3));
        ws.setMileageKm(180234);

        SparePartUsage spu1 = new SparePartUsage();
        spu1.setId(50000L);
        spu1.setQuantity(BigDecimal.ONE);
        spu1.setOrderId(2000L);
        SparePartUsage spu2 = new SparePartUsage();
        spu2.setId(50001L);
        spu2.setQuantity(BigDecimal.TWO);
        spu2.setOrderId(2001L);
        ws.setSparePartUsages(List.of(spu1, spu2));

        WorkshopServiceRepository wsRepo = mock(WorkshopServiceRepository.class);
        when(wsRepo.save(ws)).thenReturn(Mono.just(ws));

        SparePartUsageRepository spuRepo = mock(SparePartUsageRepository.class);
        when(spuRepo.saveAll(ws.getSparePartUsages())).thenReturn(Flux.just(spu1, spu2));

        TransactionService txService = new TransactionService();
        ReflectionTestUtils.setField(txService, "workshopServiceRepository", wsRepo);
        ReflectionTestUtils.setField(txService, "sparePartUsageRepository", spuRepo);

        StepVerifier.create(txService.save(ws))
                .expectNext(ws)
                .expectComplete()
                .verify();
    }

    @Test
    void findAllWithBothMigDataAndSparePart() {
        WorkshopService ws1 = new WorkshopService();
        ws1.setId(1000L);
        ws1.setVehicleId(1000L);
        ws1.setStartDate(LocalDate.of(2020, 12, 24));
        ws1.setCreationDate(LocalDate.of(2021, 1, 1));
        ws1.setMileageKm(180234);

        WorkshopService ws2 = new WorkshopService();
        ws2.setId(1001L);
        ws2.setVehicleId(1001L);
        ws2.setStartDate(LocalDate.of(2021, 12, 24));
        ws2.setCreationDate(LocalDate.of(2022, 1, 1));
        ws2.setMileageKm(90887);

        // for workshop
        ArgumentCaptor<Sort> sortArgumentCaptor = ArgumentCaptor.forClass(Sort.class);
        WorkshopServiceRepository wsRepo = mock(WorkshopServiceRepository.class);
        when(wsRepo.findAll(sortArgumentCaptor.capture())).thenReturn(Flux.just(ws1, ws2));

        // for workshop service 1
        MigData md1 = new MigData();
        md1.setPartName("OIL FILTER");
        MigData md2 = new MigData();
        md2.setPartName("FUEL FILTER");

        MigDataRepository mdRepo = mock(MigDataRepository.class);
        when(mdRepo.findByServiceId(1000L)).thenReturn(Flux.just(md1, md2));

        // for workshop service 2
        MigData md3 = new MigData();
        md3.setPartName("BRAKE LINING");
        MigData md4 = new MigData();
        md4.setPartName("6MM SCREW");

        when(mdRepo.findByServiceId(1001L)).thenReturn(Flux.just(md3, md4));

        // spare part usage
        // for workshop service 1

        SparePartUsage spu1 = new SparePartUsage();
        spu1.setOrderId(5000L);
        spu1.setQuantity(BigDecimal.ONE);

        SparePartUsage spu2 = new SparePartUsage();
        spu2.setOrderId(5001L);
        spu2.setQuantity(BigDecimal.valueOf(5));

        SparePartUsageRepository spuRepo = mock(SparePartUsageRepository.class);
        when(spuRepo.findByServiceId(1000L)).thenReturn(Flux.just(spu1, spu2));

        SparePartUsage spu3 = new SparePartUsage();
        spu3.setOrderId(5002L);
        spu3.setQuantity(BigDecimal.TEN);

        SparePartUsage spu4 = new SparePartUsage();
        spu4.setOrderId(5003L);
        spu4.setQuantity(BigDecimal.valueOf(5));

        when(spuRepo.findByServiceId(1001L)).thenReturn(Flux.just(spu3, spu4));

        WorkshopServiceMediaService workshopServiceMediaService = mock(WorkshopServiceMediaService.class);
        when(workshopServiceMediaService.groupedServiceIdCounts()).thenReturn(Mono.just(Collections.emptyMap()));

        TransactionService txService = new TransactionService();
        ReflectionTestUtils.setField(txService, "workshopServiceRepository", wsRepo);
        ReflectionTestUtils.setField(txService, "sparePartUsageRepository", spuRepo);
        ReflectionTestUtils.setField(txService, "migDataRepository", mdRepo);
        ReflectionTestUtils.setField(txService, "workshopServiceMediaService", workshopServiceMediaService);

        StepVerifier.create(txService.findAll())
                .expectNextMatches(wss ->
                    wss.getMigratedHandWrittenSpareParts().getFirst().getPartName().equals("OIL FILTER") &&
                            wss.getMigratedHandWrittenSpareParts().get(1).getPartName().equals("FUEL FILTER") &&
                            wss.getSparePartUsages().getFirst().getOrderId().equals(5000L) &&
                            wss.getSparePartUsages().get(1).getOrderId().equals(5001L)
                )
                .expectNextMatches(wss ->
                        wss.getMigratedHandWrittenSpareParts().getFirst().getPartName().equals("BRAKE LINING") &&
                                wss.getMigratedHandWrittenSpareParts().get(1).getPartName().equals("6MM SCREW") &&
                                wss.getSparePartUsages().getFirst().getOrderId().equals(5002L) &&
                                wss.getSparePartUsages().get(1).getOrderId().equals(5003L)
                )
                .verifyComplete();
    }
}
