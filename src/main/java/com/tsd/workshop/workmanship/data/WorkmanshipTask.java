package com.tsd.workshop.workmanship.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("workmanship_task")
public class WorkmanshipTask {

    @Id
    private Long id;

    private LocalDate recordedDate;

    private Long serviceId;

    private Long taskId; // task template id

    private String[] foremen;

    private String remarks;

    private BigDecimal quotedPrice;

    private Double actualDurationHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(LocalDate recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String[] getForemen() {
        return foremen;
    }

    public void setForemen(String[] foremen) {
        this.foremen = foremen;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(BigDecimal quotedPrice) {
        this.quotedPrice = quotedPrice;
    }

    public Double getActualDurationHours() {
        return actualDurationHours;
    }

    public void setActualDurationHours(Double actualDurationHours) {
        this.actualDurationHours = actualDurationHours;
    }

    @Override
    public String toString() {
        return "WorkmanshipTask{" +
                "id=" + id +
                ", serviceId=" + serviceId +
                ", taskId=" + taskId +
                '}';
    }
}
