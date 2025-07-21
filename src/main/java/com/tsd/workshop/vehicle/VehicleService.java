package com.tsd.workshop.vehicle;

import com.tsd.workshop.vehicle.data.Vehicle;
import com.tsd.workshop.vehicle.data.VehicleRepository;
import com.tsd.workshop.vehicle.data.VehicleSqlRepository;
import com.tsd.workshop.vehicle.fleet.FleetInfo;
import com.tsd.workshop.vehicle.fleet.FleetInfosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleSqlRepository vehicleSqlRepository;

    @Autowired
    private FleetInfosService fleetInfosService;

    public Mono<Vehicle> saveVehicle(Vehicle vehicle) {
        return vehicleRepository.findByVehicleNo(vehicle.getVehicleNo())
                .flatMap(veh -> {
                    if (!veh.getId().equals(vehicle.getId())) {
                        throw new VehicleAlreadyExistsException(veh);
                    }
                    return Mono.just(vehicle);
                })
                .switchIfEmpty(Mono.just(vehicle))
                .flatMap(veh -> vehicleRepository.save(veh)
                        .flatMap(stored -> vehicleRepository.findById(vehicle.getId())));
    }

    public Mono<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Flux<Vehicle> findAll() {
        return vehicleRepository.findAll(Sort.by(Sort.Order.asc("companyId").nullsLast(),
                Sort.Order.desc("latestMileageKm").nullsLast(),
                Sort.Order.asc("vehicleNo")));
    }

    public Flux<Vehicle> findAllOfInternal(VehicleStatus vehicleStatus) {
        return vehicleRepository.findAllOfInternal(vehicleStatus);
    }

    public Mono<Void> deleteById(Long id) {
        return vehicleRepository.deleteById(id);
    }

    public Flux<FleetInfo> fleetInfosOfVehicle(Long vehicleId) {
        return fleetInfosService.pollForStaleFleetInfos(vehicleId)
                .thenMany(vehicleSqlRepository.fleetInfos(vehicleId));
    }
}