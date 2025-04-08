package com.tsd.workshop.transaction.data;

import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.workmanship.data.WorkmanshipTask;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table("workshop_service")
public class WorkshopService {

    @Id
    private Long id;

    private Long vehicleId;

    private LocalDate startDate;

    private LocalDate completionDate;

    private LocalDate creationDate;

    private List<WorkmanshipTask> tasks;

    private List<SparePartUsage> sparePartUsages;
}
