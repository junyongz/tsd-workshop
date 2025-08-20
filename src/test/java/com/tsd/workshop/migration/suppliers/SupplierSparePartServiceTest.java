package com.tsd.workshop.migration.suppliers;

import com.tsd.workshop.migration.suppliers.data.SupplierSparePartSqlRepository;
import com.tsd.workshop.transaction.utilization.data.SparePartUsageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class SupplierSparePartServiceTest {

    @Test
    void deleteById() {
        SupplierSparePartService supplierSparePartService = new SupplierSparePartService();

        SupplierSparePartSqlRepository supplierSparePartSqlRepository = mock(SupplierSparePartSqlRepository.class);
        when(supplierSparePartSqlRepository.moveToDeletedTable(1000L)).thenReturn(Mono.just(1L));
        ReflectionTestUtils.setField(supplierSparePartService, "supplierSparePartSqlRepository", supplierSparePartSqlRepository);

        SparePartUsageRepository sparePartUsageRepository = mock(SparePartUsageRepository.class);
        when(sparePartUsageRepository.deleteByOrderId(1000L)).thenReturn(Mono.empty());
        ReflectionTestUtils.setField(supplierSparePartService, "sparePartUsageRepository", sparePartUsageRepository);

        Mono<Void> voidz = supplierSparePartService.deleteById(1000L);

        StepVerifier.create(voidz).expectComplete().verify();

        verify(sparePartUsageRepository).deleteByOrderId(1000L);}
}
