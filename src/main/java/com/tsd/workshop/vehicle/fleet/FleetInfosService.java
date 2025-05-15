package com.tsd.workshop.vehicle.fleet;

import com.tsd.workshop.vehicle.data.VehicleSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class FleetInfosService {

    private final Logger logger = LoggerFactory.getLogger(FleetInfosService.class);

    private final List<FleetInfoFetcher> fetchers;

    private final VehicleSqlRepository vehicleSqlRepository;

    public FleetInfosService(List<FleetInfoFetcher> fetchers, VehicleSqlRepository vehicleSqlRepository) {
        this.fetchers = fetchers;
        this.vehicleSqlRepository = vehicleSqlRepository;
    }

    @Scheduled(cron = "${vehicle.fleet.info.fetch.cron}")
    public void pollingFleetInfo() {
        fetching().subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    @Transactional
    public Mono<Long> updateVehicleFleetInfo(FleetInfo fleetInfo) {
        return this.vehicleSqlRepository.updateVehicleLatestMileageKm(fleetInfo)
                .flatMap(count -> this.vehicleSqlRepository.recordVehicleFleetInfo(fleetInfo));
    }

    @Transactional
    public Mono<Void> pollForStaleFleetInfos(Long vehicleId) {
        return this.vehicleSqlRepository.staleFleetInfoCount(vehicleId)
                .flatMap(cnt -> {
                    if (cnt > 0) {
                        return fetching().then(Mono.empty());
                    }
                    return Mono.empty();
                });
    }

    private Flux<FleetInfo> fetching() {
        return Flux.fromIterable(fetchers)
                .flatMap(FleetInfoFetcher::fetch)
                .flatMap(fleetInfo -> updateVehicleFleetInfo(fleetInfo).map(count -> {
                    logger.info("{} record insert for data: {}", count, fleetInfo);
                    return fleetInfo;
                }));
    }
}
