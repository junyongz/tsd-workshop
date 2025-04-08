package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageR2dbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SparePartUsageServiceTest {

    @Test
    void allOkayForEditing() {
        SparePartUsageR2dbcRepository sparePartUsageR2dbcRepository = mock(SparePartUsageR2dbcRepository.class);
        when(sparePartUsageR2dbcRepository.usageByOrderId(1000L)).thenReturn(Mono.just(0));
        when(sparePartUsageR2dbcRepository.usageByOrderId(1010L)).thenReturn(Mono.just(0));
        when(sparePartUsageR2dbcRepository.usageByOrderId(1020L)).thenReturn(Mono.just(0));

        SparePartUsageService sparePartUsageService = new SparePartUsageService();
        ReflectionTestUtils.setField(sparePartUsageService, "sparePartUsageR2dbcRepository", sparePartUsageR2dbcRepository);

        SupplierSparePart ssp1 = new SupplierSparePart();
        ssp1.setId(1000L);
        SupplierSparePart ssp2 = new SupplierSparePart();
        ssp2.setId(1010L);
        SupplierSparePart ssp3 = new SupplierSparePart();
        ssp3.setId(1020L);

        List<SupplierSparePart> ssps = List.of(ssp1, ssp2, ssp3);
        Flux<Boolean> results = sparePartUsageService.validateSparePartUsageQuantityForEditing(ssps);

        StepVerifier.create(results)
                .expectNext(true, true, true)
                .verifyComplete();
    }

    @Test
    void oneNotOkayForEditing() {
        SparePartUsageR2dbcRepository sparePartUsageR2dbcRepository = mock(SparePartUsageR2dbcRepository.class);
        when(sparePartUsageR2dbcRepository.usageByOrderId(1000L)).thenReturn(Mono.just(0));
        when(sparePartUsageR2dbcRepository.usageByOrderId(1010L)).thenReturn(Mono.just(0));
        when(sparePartUsageR2dbcRepository.usageByOrderId(1020L)).thenReturn(Mono.just(1));

        SparePartUsageService sparePartUsageService = new SparePartUsageService();
        ReflectionTestUtils.setField(sparePartUsageService, "sparePartUsageR2dbcRepository", sparePartUsageR2dbcRepository);

        SupplierSparePart ssp1 = new SupplierSparePart();
        ssp1.setId(1000L);
        SupplierSparePart ssp2 = new SupplierSparePart();
        ssp2.setId(1010L);
        SupplierSparePart ssp3 = new SupplierSparePart();
        ssp3.setId(1020L);

        List<SupplierSparePart> ssps = List.of(ssp1, ssp2, ssp3);
        Flux<Boolean> results = sparePartUsageService.validateSparePartUsageQuantityForEditing(ssps);

        StepVerifier.create(results)
                .expectNext(true, true)
                .expectError(QuantityNotMatchedException.class)
                .verify();
    }

    @Test
    void emptyForEditing() {
        SparePartUsageService sparePartUsageService = new SparePartUsageService();

        Flux<Boolean> results = sparePartUsageService.validateSparePartUsageQuantityForEditing(Collections.emptyList());

        StepVerifier.create(results)
                .expectNext(true)
                .verifyComplete();
    }
}
