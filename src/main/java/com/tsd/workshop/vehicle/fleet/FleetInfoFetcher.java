package com.tsd.workshop.vehicle.fleet;

import reactor.core.publisher.Flux;

public interface FleetInfoFetcher {
    Flux<FleetInfo> fetch();
}
