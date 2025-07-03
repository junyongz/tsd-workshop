package com.tsd.workshop.sparepart.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;

import java.time.LocalDate;
import java.util.List;

// order to link to this spare part
public class SparePart {
    @Id
    private Long id;
    private String partNo;
    private String partName;
    private String description;
    @InsertOnlyProperty
    private LocalDate creationDate;
    private List<OEM> oems;
    private List<Truck> compatibleTrucks;
    @Transient
    private List<Long> orderIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<OEM> getOems() {
        return oems;
    }

    public void setOems(List<OEM> oems) {
        this.oems = oems;
    }

    public List<Truck> getCompatibleTrucks() {
        return compatibleTrucks;
    }

    public void setCompatibleTrucks(List<Truck> compatibleTrucks) {
        this.compatibleTrucks = compatibleTrucks;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }
}
