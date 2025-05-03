package com.tsd.workshop.vehicle.data;

import com.tsd.workshop.vehicle.fleet.FleetInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class VehicleSqlRepository {

    private final DatabaseClient databaseClient;

    private final int maxRecordNum;

    public VehicleSqlRepository(DatabaseClient databaseClient, @Value("${vehicle.fleet.info.fetch.max.record:72}") int maxRecordNum) {
        this.databaseClient = databaseClient;
        this.maxRecordNum = maxRecordNum;
    }

    @Transactional
    public Mono<Long> recordVehicleFleetInfo(FleetInfo fleetInfo) {
        return databaseClient.sql("select count(*) as cnt from vehicle_fleet_info where vehicle_no = :vehicle_no")
                .bind(0, fleetInfo.getVehicleNo())
                .fetch()
                .first()
                .switchIfEmpty(Mono.just(Map.of("cnt", 0L)))
                .map(row -> (Long) row.get("cnt"))
                .flatMap(count -> {
                    Mono<Long> insert = databaseClient.sql("""
                        insert into vehicle_fleet_info (vehicle_id, vehicle_no, creation_date, data) values
                        ((select id from vehicle where plate_no = :vehicle_no), :vehicle_no, :creation_date, :fleet_info::jsonb)
                        """).bind("vehicle_no", fleetInfo.getVehicleNo())
                            .bind("creation_date", LocalDateTime.now())
                            .bind("fleet_info", fleetInfo.asJson())
                            .fetch()
                            .rowsUpdated();

                    if (count >= maxRecordNum) {
                        return databaseClient.sql("""
                                delete from vehicle_fleet_info where id in
                                (select id from vehicle_fleet_info where vehicle_no = :vehicle_no
                                order by to_timestamp((data->>'recordedDateTime'), 'YYYY-MM-DD"T"HH24:MI:SS') desc
                                offset :max_record_num
                                )
                        """).bind("vehicle_no", fleetInfo.getVehicleNo())
                                .bind("max_record_num", maxRecordNum)
                                .fetch()
                                .rowsUpdated()
                                .flatMap(deleteCount -> insert);
                    }

                    return insert;

                });
    }

    @Transactional
    public Mono<Long> updateVehicleLatestMileageKm(FleetInfo fleetInfo) {
        return databaseClient.sql("update vehicle set latest_mileage_km = :latest_mileage_km where plate_no = :vehicle_no")
                .bind(0, fleetInfo.getMileageKm())
                .bind(1, fleetInfo.getVehicleNo())
                .fetch()
                .rowsUpdated();
    }
}
