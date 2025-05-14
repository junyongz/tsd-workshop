package com.tsd.workshop.vehicle;

import com.tsd.workshop.vehicle.data.Vehicle;
import com.tsd.workshop.vehicle.data.VehicleRepository;
import com.tsd.workshop.vehicle.data.VehicleSqlRepository;
import com.tsd.workshop.vehicle.fleet.FleetInfo;
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

    public Mono<Vehicle> saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle)
                .flatMap(veh -> vehicleRepository.findById(vehicle.getId()));
    }

    public Mono<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Flux<Vehicle> findAll() {
        return vehicleRepository.findAll(Sort.by(Sort.Order.asc("companyId").nullsLast(),
                Sort.Order.desc("latestMileageKm").nullsLast(),
                Sort.Order.asc("vehicleNo")));
    }

    public Flux<Vehicle> findAllOfInternal() {
        return vehicleRepository.findAllOfInternal();
    }

    public Mono<Void> deleteById(Long id) {
        return vehicleRepository.deleteById(id);
    }

    public Flux<FleetInfo> fleetInfosOfVehicle(Long vehicleId) {
        return vehicleSqlRepository.fleetInfos(vehicleId);
    }
}