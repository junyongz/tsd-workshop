package com.tsd.workshop.transaction.data;

import com.tsd.workshop.transaction.TransactionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Repository
@Transactional
public interface WorkshopServiceRepository extends R2dbcRepository<WorkshopService, Long> {

    Flux<WorkshopService> findByVehicleIdAndCompletionDateIsNull(Long vehicleId);

    // TODO what could happen if same vehicle have more than 1 service at same day
    @Query(value = """
                select * from workshop_service where (start_date, vehicle_no) in (
                select max(start_date) last_date, vehicle_no from workshop_service
                where transaction_types @> :transaction_types
                group by vehicle_no)
            """)
    Flux<WorkshopService> findLatestByTransactionTypes(TransactionType[] transactionTypes);

    Flux<WorkshopService> findAllBy(Pageable page);

    @Query(value = """
                select * from workshop_service where extract(year from creation_date) = :year
                and extract(month from creation_date) = :month order by completion_date desc nulls first,
                creation_date desc
            """)
    Flux<WorkshopService> findByYearAndMonth(int year, int month);

    Flux<WorkshopService> findByCompletionDateIsNull();

    // be careful of what you wish for using this method!
    Flux<WorkshopService> findByCompletionDateIsNotNull();
}
