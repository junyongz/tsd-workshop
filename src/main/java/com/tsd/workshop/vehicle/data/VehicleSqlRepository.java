package com.tsd.workshop.vehicle.data;

import com.tsd.workshop.telematics.maps.render.Coordination;
import com.tsd.workshop.vehicle.fleet.FleetInfo;
import com.tsd.workshop.vehicle.fleet.FuelEfficiency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Repository
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
                                order by creation_date desc
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

    public Flux<FleetInfo> fleetInfos(Long vehicleId) {
        return databaseClient.sql("""
                select vehicle_no,
                to_timestamp((data->>'recordedDateTime'), 'YYYY-MM-DD"T"HH24:MI:SS') recorded_date_time,
                (data->>'mileageKm')::int mileage_km,
                (data->>'remainingFuelLitre')::numeric remaining_fuel_litre,
                (data->'coordination'->>'latitude')::numeric latitude,
                (data->'coordination'->>'longitude')::numeric longitude
                from vehicle_fleet_info where vehicle_id = :vehicle_id
                """)
                .bind(0, vehicleId)
                .fetch()
                .all()
                .map(row ->
                     new FleetInfo(
                            (String) row.get("vehicle_no"),
                             ((OffsetDateTime) row.get("recorded_date_time")).toLocalDateTime(),
                            (Integer) row.get("mileage_km"),
                             ((BigDecimal) row.get("remaining_fuel_litre")).doubleValue(),
                            new Coordination(((BigDecimal) row.get("latitude")).doubleValue(), ((BigDecimal) row.get("longitude")).doubleValue())
                    )
                );
    }

    public Mono<Long> staleFleetInfoCount(long vehicleId) {
        return databaseClient.sql("""
                select count(*) cnt from (
                  select vehicle_no, vehicle_id,
                  max(to_timestamp((data->>'recordedDateTime'), 'YYYY-MM-DD"T"HH24:MI:SS')) max_recorded
                  from vehicle_fleet_info
                  group by  vehicle_no, vehicle_id) latest_record
                where vehicle_id = :vehicle_id
                and max_recorded < now() - interval '15 minutes'
                """)
                .bind(0, vehicleId)
                .fetch()
                .first()
                .map(row -> (Long) row.get("cnt"));
    }

    public Flux<FuelEfficiency> fuelEfficiencies() {
        return databaseClient.sql("""
                WITH CleanedData AS (
                    SELECT vehicle_no, (data->>'mileageKm')::int mileage, AVG((data->>'remainingFuelLitre')::numeric) AS fuel
                    FROM vehicle_fleet_info
                    GROUP BY vehicle_no, mileage
                ),
                ProcessedData AS (
                    SELECT
                        vehicle_no,
                        mileage,
                        fuel,
                        LAG(mileage) OVER (PARTITION BY vehicle_no ORDER BY mileage) AS prev_mileage,
                        LAG(fuel) OVER (PARTITION BY vehicle_no ORDER BY mileage) AS prev_fuel
                    FROM CleanedData
                ),
                EfficiencyCalc AS (
                    SELECT
                        vehicle_no,
                        mileage,
                        fuel,
                        prev_mileage,
                        prev_fuel,
                        (mileage - prev_mileage) AS distance_km,
                        CASE
                            WHEN prev_fuel IS NOT NULL AND fuel < prev_fuel THEN prev_fuel - fuel
                            ELSE NULL
                        END AS fuel_consumed_liters,
                        CASE
                            WHEN prev_fuel IS NOT NULL AND fuel > prev_fuel + 10 THEN 1
                            ELSE 0
                        END AS is_refill
                    FROM ProcessedData
                    WHERE prev_mileage IS NOT NULL
                ),
                Efficiency AS (
                    SELECT
                        vehicle_no,
                        mileage,
                        fuel,
                        distance_km,
                        fuel_consumed_liters,
                        is_refill,
                        CASE
                            WHEN is_refill = 0 AND distance_km > 0 THEN fuel_consumed_liters / distance_km
                            ELSE NULL
                        END AS fuel_efficiency_l_per_km
                    FROM EfficiencyCalc
                ),
                ValidEfficiencies AS (
                    SELECT
                        vehicle_no,
                        fuel_efficiency_l_per_km
                    FROM Efficiency
                    WHERE fuel_efficiency_l_per_km IS NOT NULL
                )
                SELECT
                    vehicle_no,
                    AVG(fuel_efficiency_l_per_km) AS avg_fuel_efficiency_l_per_km,
                    PERCENTILE_CONT(0.9) WITHIN GROUP (ORDER BY fuel_efficiency_l_per_km) AS p90_fuel_efficiency_l_per_km
                FROM ValidEfficiencies
                GROUP BY vehicle_no
                ORDER BY avg_fuel_efficiency_l_per_km asc
                """)
                .fetch()
                .all()
                .map(row -> new FuelEfficiency((String) row.get("vehicle_no"),
                        ((BigDecimal)row.get("avg_fuel_efficiency_l_per_km")).doubleValue()));

    }
}
