package com.tsd.workshop.transaction.data;

import com.tsd.workshop.transaction.TransactionType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface WorkshopServiceRepository extends R2dbcRepository<WorkshopService, Long> {

    Flux<WorkshopService> findByVehicleIdAndCompletionDateIsNull(Long vehicleId);

    @Query(value = """
                select * from workshop_service where (start_date, vehicle_no) in (
                select max(start_date) last_date, vehicle_no from workshop_service
                where transaction_types @> :transaction_types
                group by vehicle_no)
            """)
    Flux<WorkshopService> findLatestByTransactionTypes(TransactionType[] transactionTypes);
}
