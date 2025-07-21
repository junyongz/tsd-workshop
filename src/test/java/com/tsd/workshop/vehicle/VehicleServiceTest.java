package com.tsd.workshop.vehicle;

import com.tsd.workshop.vehicle.data.Vehicle;
import com.tsd.workshop.vehicle.data.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VehicleServiceTest {

    @Test
    void duplicateCheckNonExists() {
        VehicleRepository repo = mock(VehicleRepository.class);
        when(repo.findByVehicleNo("JJ 23")).thenReturn(Mono.empty());

        VehicleService vehicleService = new VehicleService();
        ReflectionTestUtils.setField(vehicleService, "vehicleRepository", repo);

        Vehicle veh = new Vehicle();
        veh.setVehicleNo("JJ 23");

        when(repo.save(veh)).thenAnswer((args) -> {
            Vehicle stored = (Vehicle) args.getArguments()[0];
            stored.setId(2000L);
            return Mono.just(stored);
        });
        when(repo.findById(2000L)).thenReturn(Mono.just(veh));

        StepVerifier.create(vehicleService.saveVehicle(veh))
                .expectNext(veh)
                .expectComplete()
                .verify();
    }

    @Test
    void duplicateCheckExists() {
        Vehicle existing = new Vehicle();
        existing.setId(2300L);
        existing.setVehicleNo("JJ 23");

        VehicleRepository repo = mock(VehicleRepository.class);
        when(repo.findByVehicleNo("JJ 23")).thenReturn(Mono.just(existing));

        VehicleService vehicleService = new VehicleService();
        ReflectionTestUtils.setField(vehicleService, "vehicleRepository", repo);

        Vehicle veh = new Vehicle();
        veh.setVehicleNo("JJ 23");

        StepVerifier.create(vehicleService.saveVehicle(veh))
                .expectErrorSatisfies(ex -> {
                    assertThat(ex).isInstanceOf(VehicleAlreadyExistsException.class);
                })
                .verify();
    }

    @Test
    void duplicateCheckExistsWithAnotherId() {
        Vehicle existing = new Vehicle();
        existing.setId(2300L);
        existing.setVehicleNo("JJ 23");

        VehicleRepository repo = mock(VehicleRepository.class);
        when(repo.findByVehicleNo("JJ 23")).thenReturn(Mono.just(existing));

        VehicleService vehicleService = new VehicleService();
        ReflectionTestUtils.setField(vehicleService, "vehicleRepository", repo);

        Vehicle veh = new Vehicle();
        veh.setId(2400L);
        veh.setVehicleNo("JJ 23");

        StepVerifier.create(vehicleService.saveVehicle(veh))
                .expectErrorSatisfies(ex -> {
                    assertThat(ex).isInstanceOf(VehicleAlreadyExistsException.class);
                })
                .verify();
    }

    @Test
    void duplicateCheckExistsWithSameId() {
        Vehicle existing = new Vehicle();
        existing.setId(2300L);
        existing.setVehicleNo("JJ 23");

        VehicleRepository repo = mock(VehicleRepository.class);
        when(repo.findByVehicleNo("JJ 23")).thenReturn(Mono.just(existing));

        VehicleService vehicleService = new VehicleService();
        ReflectionTestUtils.setField(vehicleService, "vehicleRepository", repo);

        Vehicle veh = new Vehicle();
        veh.setId(2300L);
        veh.setVehicleNo("JJ 23");

        when(repo.save(veh)).thenAnswer((args) -> {
            Vehicle stored = (Vehicle) args.getArguments()[0];
            return Mono.just(stored);
        });
        when(repo.findById(2300L)).thenReturn(Mono.just(veh));

        StepVerifier.create(vehicleService.saveVehicle(veh))
                .expectNext(veh)
                .expectComplete()
                .verify();
    }
}
