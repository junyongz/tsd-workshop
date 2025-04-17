package com.tsd.workshop.migration.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("mig_data")
public class MigData {
    @Id
    private Long index;
    private String sheetName;
    private String vehicleNo;
    private LocalDate creationDate;
    private String itemDescription;
    private String partName;
    private Integer quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BigDecimal calculatedTotalPrice;
    @Column("migrated_ind")
    private Boolean migratedIndicator = Boolean.FALSE;
    private LocalDate completionDate;
    private Long orderId; // link to the order for the spare parts
    private Long supplierId; // link to the supplier of the spare parts
    private Long serviceId;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getCalculatedTotalPrice() {
        return calculatedTotalPrice;
    }

    public void setCalculatedTotalPrice(BigDecimal calculatedTotalPrice) {
        this.calculatedTotalPrice = calculatedTotalPrice;
    }

    public Boolean getMigratedIndicator() {
        return migratedIndicator;
    }

    public void setMigratedIndicator(Boolean migratedIndicator) {
        this.migratedIndicator = migratedIndicator;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @JsonIgnore
    public boolean isCompleted() {
        return this.completionDate != null;
    }

    public SparePartUsage toSparePartUsage() {
        SparePartUsage spu = new SparePartUsage();
        spu.setQuantity(this.quantity);
        spu.setServiceId(this.index);
        spu.setVehicleNo(this.vehicleNo);
        spu.setOrderId(this.orderId);
        spu.setUsageDate(this.creationDate);
        return spu;
    }

    public void afterRecordUsage(SparePartUsage spu) {
        this.quantity = spu.getQuantity();
        this.vehicleNo = spu.getVehicleNo();
        this.creationDate= spu.getUsageDate();
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(this.quantity));
    }
}