package com.tsd.workshop.workmanship.template.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("workshop_task")
public class Task {

    @Id
    private Long id;

    private Long componentId;

    private TaskTypeCategory category;

    private String workmanshipTask;

    private BigDecimal unitPrice;

    private Double labourHours;

    private TaskComplexity complexity;

    private String description;

    @Transient
    private TaskComponent component;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public TaskTypeCategory getCategory() {
        return category;
    }

    public void setCategory(TaskTypeCategory category) {
        this.category = category;
    }

    public String getWorkmanshipTask() {
        return workmanshipTask;
    }

    public void setWorkmanshipTask(String workmanshipTask) {
        this.workmanshipTask = workmanshipTask;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getLabourHours() {
        return labourHours;
    }

    public void setLabourHours(Double labourHours) {
        this.labourHours = labourHours;
    }

    public TaskComplexity getComplexity() {
        return complexity;
    }

    public void setComplexity(TaskComplexity complexity) {
        this.complexity = complexity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskComponent getComponent() {
        return component;
    }

    public void setComponent(TaskComponent component) {
        this.component = component;
    }
}
