package com.tsd.workshop.migration.suppliers;

import com.tsd.workshop.migration.MigDataService;
import com.tsd.workshop.migration.spareparts.MigSparePartService;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePartR2dbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class SupplierSparePartServiceTest {

    @Test
    void deleteByIdWithNoMigData() {
        SupplierSparePartService supplierSparePartService = new SupplierSparePartService();

        SupplierSparePartR2dbcRepository supplierSparePartR2dbcRepository = mock(SupplierSparePartR2dbcRepository.class);
        when(supplierSparePartR2dbcRepository.moveToDeletedTable(1000L)).thenReturn(Mono.just(1L));
        ReflectionTestUtils.setField(supplierSparePartService, "supplierSparePartR2dbcRepository", supplierSparePartR2dbcRepository);

        MigDataService migDataService = mock(MigDataService.class);
        when(migDataService.deleteByOrderId(1000L)).thenReturn(Mono.empty());
        ReflectionTestUtils.setField(supplierSparePartService, "migDataService", migDataService);

        MigSparePartService migSparePartService = mock(MigSparePartService.class);
        when(migSparePartService.deleteByOrderId(1000L)).thenReturn(Mono.empty());
        ReflectionTestUtils.setField(supplierSparePartService, "migSparePartService", migSparePartService);

        Mono<Void> voidz = supplierSparePartService.deleteById(1000L);

        StepVerifier.create(voidz).expectComplete().verify();

        verify(migDataService).deleteByOrderId(1000L);
        verify(migSparePartService).deleteByOrderId(1000L);
    }

    @Test
    void deleteById() {
        SupplierSparePartService supplierSparePartService = new SupplierSparePartService();

        SupplierSparePartR2dbcRepository supplierSparePartR2dbcRepository = mock(SupplierSparePartR2dbcRepository.class);
        when(supplierSparePartR2dbcRepository.moveToDeletedTable(1000L)).thenReturn(Mono.just(1L));
        ReflectionTestUtils.setField(supplierSparePartService, "supplierSparePartR2dbcRepository", supplierSparePartR2dbcRepository);

        MigDataService migDataService = mock(MigDataService.class);
        when(migDataService.deleteByOrderId(1000L)).thenReturn(Mono.just(1L));
        ReflectionTestUtils.setField(supplierSparePartService, "migDataService", migDataService);

        MigSparePartService migSparePartService = mock(MigSparePartService.class);
        when(migSparePartService.deleteByOrderId(1000L)).thenReturn(Mono.empty());
        ReflectionTestUtils.setField(supplierSparePartService, "migSparePartService", migSparePartService);

        Mono<Void> voidz = supplierSparePartService.deleteById(1000L);

        StepVerifier.create(voidz).expectComplete().verify();

        verify(migDataService).deleteByOrderId(1000L);
        verify(migSparePartService).deleteByOrderId(1000L);
    }
}
