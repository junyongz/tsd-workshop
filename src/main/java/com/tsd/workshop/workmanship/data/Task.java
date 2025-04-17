package com.tsd.workshop.workmanship.data;

import com.tsd.workshop.vehicle.Subsystem;
import com.tsd.workshop.vehicle.Subsystems;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Duration;


// TODO to add priority or difficulty, dependencies
@Table("workshop_task")
public class Task {

    @Id
    private Long id;

    private Subsystems subsystem;

    private Subsystem component;

    private TaskType taskType;

    // TODO point to the spare part of component
    private String sparePart;

    private String remarks;

    private BigDecimal unitPrice;

    private Duration estimatedDuration;
}
