package com.tsd.workshop.vehicle.web;

import com.tsd.workshop.vehicle.VehicleNoWrongFormatException;
import com.tsd.workshop.vehicle.VehicleService;
import com.tsd.workshop.vehicle.data.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public Mono<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        if (!Pattern.matches("([A-Z]{1,3})\\s(\\d{1,4})(\\s([A-Z]{1,2}))?", vehicle.getVehicleNo())) {
            throw new VehicleNoWrongFormatException(vehicle);
        }
        return vehicleService.saveVehicle(vehicle);
    }

    @GetMapping("/{id}")
    public Mono<Vehicle> getVehicle(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @GetMapping
    public Flux<Vehicle> getAllVehicles(@RequestParam(value = "internal", required = false, defaultValue = "false") Boolean internal)
    {
        if (internal) {
            return vehicleService.findAllOfInternal();
        }
        return vehicleService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteById(id);
    }
}