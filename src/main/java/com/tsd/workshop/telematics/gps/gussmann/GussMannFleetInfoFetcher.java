package com.tsd.workshop.telematics.gps.gussmann;

import com.tsd.workshop.vehicle.fleet.FleetInfoFetcher;
import com.tsd.workshop.telematics.maps.render.Coordination;
import com.tsd.workshop.vehicle.fleet.FleetInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.StreamSupport;

@Component
public class GussMannFleetInfoFetcher implements FleetInfoFetcher {

    private final GussMannFleetWebClient gussMannFleetWebClient;

    public GussMannFleetInfoFetcher(GussMannFleetWebClient gussMannFleetWebClient) {
        this.gussMannFleetWebClient = gussMannFleetWebClient;
    }

    @Override
    public Flux<FleetInfo> fetch() {
        return gussMannFleetWebClient.fleetInfo()
                .flatMapMany(jsonNode -> Flux.fromIterable(StreamSupport.stream(jsonNode.get("aaData").spliterator(), false).toList()))
                .map(jn -> {
                    String possibleVehicleNo = jn.get(2).asText();
                    possibleVehicleNo = possibleVehicleNo.replaceAll("(\\p{L}+)(\\d+)", "$1 $2");

                    String dateTimeVal = jn.get(5).asText();
                    LocalDateTime recordedDateTime = LocalDateTime.parse(dateTimeVal.trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

                    int odometerKm = jn.get(10).asInt();

                    double remainingFuelLitre1 = jn.get(12).asDouble();
                    if (remainingFuelLitre1 < 0) {
                        remainingFuelLitre1 = 0;
                    }
                    double remainingFuelLitre2 = jn.get(13).asDouble();
                    if (remainingFuelLitre2 < 0) {
                        remainingFuelLitre2 = 0;
                    }

                    double latitude = jn.get(15).asDouble();
                    double longitude = jn.get(16).asDouble();

                    return new FleetInfo(possibleVehicleNo, recordedDateTime, odometerKm,(remainingFuelLitre1 + remainingFuelLitre2), Coordination.of(latitude, longitude));
                })
                .sort(Comparator.comparing(FleetInfo::getVehicleNo));
    }
}
