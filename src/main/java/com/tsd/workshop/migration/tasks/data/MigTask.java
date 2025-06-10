package com.tsd.workshop.migration.tasks.data;

import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("raw_tasks_unit_price")
public class MigTask {

    private String description;

    private BigDecimal unitPrice;

    private String[] workshopTasks;

    private String subsystem;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String[] getWorkshopTasks() {
        return workshopTasks;
    }

    public void setWorkshopTasks(String[] workshopTasks) {
        this.workshopTasks = workshopTasks;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
}
