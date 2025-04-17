package com.tsd.workshop.workmanship.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Duration;

@Table("workmanship_tasks")
public class WorkmanshipTask {

    @Id
    private Long id;

    private Long serviceId;

    private Long taskId; // task template id

    private String[] foremen;

    private Duration actualDuration;
}
