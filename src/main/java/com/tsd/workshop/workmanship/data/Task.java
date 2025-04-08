package com.tsd.workshop.workmanship.data;

import com.tsd.workshop.vehicle.Subsystem;
import com.tsd.workshop.vehicle.Subsystems;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


// TODO to add priority or difficulty, dependencies
@Table("workshop_task")
public class Task {

    @Id
    private Long id;

    private Subsystems category;

    private Subsystem subCategory;

    private TaskType taskType;

    // TODO should it point to spare part table?
    private String sparePart;

    private String remarks;


}
