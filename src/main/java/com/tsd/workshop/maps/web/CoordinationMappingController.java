package com.tsd.workshop.maps.web;

import com.tsd.workshop.maps.render.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/mapping")
@ConditionalOnProperty(name = "google.maps.integration.enabled", havingValue = "true")
public class CoordinationMappingController {

    @Autowired
    private GoogleMapsStaticApiClient apiClient;

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public Mono<Resource> drawMap(@RequestParam("lat") double latitude,
                                  @RequestParam("long") double longitude,
                                  @RequestParam(value = "zoom", defaultValue = "14") int zoom) {
        return apiClient.staticImage(
                new ApiParameters(Location.of(Coordination.of(latitude, longitude), String.valueOf(zoom)),
                       Size.GOOGLE_MAP_MAX_SIZE)
                        .scale(Scale.TWO)
                        .add(Marker.Builder.defaultTruckMarkerWith(
                                Coordination.of(latitude, longitude))));
    }
}
