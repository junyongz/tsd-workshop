package com.tsd.workshop.migration.spareparts.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("mig_spare_parts")
public class MigSparePart {
    @Id
    private Long id;
    private String partName;
    private String unit;
    private Double unitPrice;
    private Boolean addAllowed;

    // Default constructor
    public MigSparePart() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getAddAllowed() {
        return addAllowed;
    }

    public void setAddAllowed(Boolean addAllowed) {
        this.addAllowed = addAllowed;
    }
}
