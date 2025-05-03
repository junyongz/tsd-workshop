package com.tsd.workshop.gps.gamtrack;

import com.tsd.workshop.vehicle.fleet.FleetInfoFetcher;
import com.tsd.workshop.maps.render.Coordination;
import com.tsd.workshop.vehicle.fleet.FleetInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@Component
public class GamTrackFleetInfoFetcher implements FleetInfoFetcher {

    private final GamTrackFleetWebClient gamTrackFleetWebClient;

    public GamTrackFleetInfoFetcher(GamTrackFleetWebClient gamTrackFleetWebClient) {
        this.gamTrackFleetWebClient = gamTrackFleetWebClient;
    }

    @Override
    public Flux<FleetInfo> fetch() {
        return gamTrackFleetWebClient.fleetInfo()
                .flatMapMany(doc ->
                    Flux.fromIterable(doc.selectStream("tr").toList())
                )
                .filter(elem ->
                        !elem.select("td:nth-child(11)").text().isBlank() &&
                                !elem.select("td:nth-child(11)").text()
                                        .replaceAll("[^\\d+]", "")
                                        .trim().isBlank()
                )
                .map(elem -> {
                    String possibleVehicleNo = elem.select("td:nth-child(4)").text();
                    possibleVehicleNo = possibleVehicleNo.replaceAll("\\(.*?\\)", "").trim();
                    possibleVehicleNo = possibleVehicleNo.replaceAll("(\\p{L}+)(\\d+)", "$1 $2");

                    String dateTimeVal = elem.select("td:nth-child(3)").text();
                    LocalDateTime recordedDateTime = LocalDateTime.parse(dateTimeVal.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    String odometerKmVal = elem.select("td:nth-child(11)").text();
                    Integer odometerKm = Integer.valueOf(odometerKmVal.replaceAll("[^\\d+]", ""));

                    String remainingFuelLitreVal = elem.select("td:nth-child(8)").text();
                    Double remainingFuelLitre = !remainingFuelLitreVal.isBlank() ? Double.parseDouble(remainingFuelLitreVal) : null;

                    double latitude = 0.0;
                    double longitude = 0.0;
                    String mapOnClick = elem.select("tr td:last-child a").attr("onclick");
                    for (String tokens : mapOnClick.split("&")) {
                        if (tokens.contains("lat=")) {
                            String latVal = tokens.substring(tokens.indexOf("lat=") + 4);
                            latitude = Double.parseDouble(latVal);
                        }
                        else if (tokens.contains("long=")) {
                            String longVal = tokens.substring(tokens.indexOf("long=") + 5);
                            longitude = Double.parseDouble(longVal);
                        }
                        if (latitude > 0 && longitude > 0) {
                            break;
                        }
                    }

                    return new FleetInfo(possibleVehicleNo, recordedDateTime, odometerKm, remainingFuelLitre, Coordination.of(latitude, longitude));
                })
                .sort(Comparator.comparing(FleetInfo::getVehicleNo));
    }
}
