package com.tsd.workshop.migration.suppliers.data;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.migration.spareparts.data.MigSparePart;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("mig_supplier_spare_parts")
public class SupplierSparePart {
    @Id
    private Long id;
    private String deliveryOrderNo;
    private String computedDate;
    private LocalDate invoiceDate;
    private String itemCode;
    private String partName;
    private String particular;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal unitPrice;
    private String notes;
    private Long supplierId;
    private Long sparePartId;
    private String sheetName;
    private Status status = Status.ACTIVE;

    // Default constructor
    public SupplierSparePart() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public String getComputedDate() {
        return computedDate;
    }

    public void setComputedDate(String computedDate) {
        this.computedDate = computedDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSparePartId() {
        return sparePartId;
    }

    public void setSparePartId(Long sparePartId) {
        this.sparePartId = sparePartId;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MigSparePart toSparePart() {
        MigSparePart sp = new MigSparePart();
        sp.setSupplierId(this.supplierId);
        sp.setOrderId(this.id);
        sp.setItemCode(this.itemCode);
        sp.setPartName(this.partName);
        sp.setAddAllowed(true);
        sp.setUnit(this.unit);
        sp.setUnitPrice(this.unitPrice);
        return sp;
    }

    public void saveInto(MigSparePart sp) {
        sp.setSupplierId(this.supplierId);
        sp.setOrderId(this.id);
        sp.setItemCode(this.itemCode);
        sp.setPartName(this.partName);
        sp.setAddAllowed(true);
        sp.setUnit(this.unit);
        sp.setUnitPrice(this.unitPrice);
    }

    public MigData toMigData() {
        MigData md = new MigData();
        md.setItemDescription(this.partName);
        md.setUnit(this.unit);
        md.setUnitPrice(this.unitPrice);
        md.setSupplierId(this.supplierId);
        md.setOrderId(this.id);
        return md;
    }

    @Override
    public String toString() {
        return "SupplierSparePart{" +
                "id=" + id +
                ", deliveryOrderNo='" + deliveryOrderNo + '\'' +
                ", computedDate='" + computedDate + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", partName='" + partName + '\'' +
                ", particular='" + particular + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", unitPrice=" + unitPrice +
                ", notes='" + notes + '\'' +
                ", supplierId=" + supplierId +
                ", sheetName='" + sheetName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}