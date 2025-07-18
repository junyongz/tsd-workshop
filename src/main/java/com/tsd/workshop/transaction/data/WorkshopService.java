package com.tsd.workshop.transaction.data;

import com.tsd.workshop.migration.data.MigData;
import com.tsd.workshop.transaction.TransactionType;
import com.tsd.workshop.transaction.utilization.data.SparePartUsage;
import com.tsd.workshop.workmanship.data.WorkmanshipTask;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Table("workshop_service")
public class WorkshopService {

    @Id
    private Long id;

    @InsertOnlyProperty
    private Long vehicleId;

    @InsertOnlyProperty
    private String vehicleNo;

    @InsertOnlyProperty
    private LocalDate startDate;

    private LocalDate completionDate;

    private Integer mileageKm;

    private TransactionType[] transactionTypes;

    @CreatedDate
    @InsertOnlyProperty
    private LocalDate creationDate = LocalDate.now();

    private String notes;

    private BigDecimal sparePartsMargin;

    @Transient
    private List<WorkmanshipTask> tasks;

    @Transient
    private List<SparePartUsage> sparePartUsages;

    @Transient
    private List<MigData> migratedHandWrittenSpareParts;

    @Transient
    private Integer uploadedMediasCount;

    @Transient
    private Integer sparePartsCount;

    @Transient
    private Integer workmanshipTasksCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getMileageKm() {
        return mileageKm;
    }

    public void setMileageKm(Integer mileageKm) {
        this.mileageKm = mileageKm;
    }

    public TransactionType[] getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(TransactionType[] transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getSparePartsMargin() {
        return sparePartsMargin;
    }

    public void setSparePartsMargin(BigDecimal sparePartsMargin) {
        this.sparePartsMargin = sparePartsMargin;
    }

    public List<WorkmanshipTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<WorkmanshipTask> tasks) {
        this.tasks = tasks;
    }

    public List<SparePartUsage> getSparePartUsages() {
        return sparePartUsages;
    }

    public void setSparePartUsages(List<SparePartUsage> sparePartUsages) {
        this.sparePartUsages = sparePartUsages;
    }

    public List<MigData> getMigratedHandWrittenSpareParts() {
        return migratedHandWrittenSpareParts;
    }

    public void setMigratedHandWrittenSpareParts(List<MigData> migratedHandWrittenSpareParts) {
        this.migratedHandWrittenSpareParts = migratedHandWrittenSpareParts;
    }

    public Integer getUploadedMediasCount() {
        return uploadedMediasCount;
    }

    public void setUploadedMediasCount(Integer uploadedMediasCount) {
        this.uploadedMediasCount = uploadedMediasCount;
    }

    public Integer getSparePartsCount() {
        return sparePartsCount;
    }

    public void setSparePartsCount(Integer sparePartsCount) {
        this.sparePartsCount = sparePartsCount;
    }

    public Integer getWorkmanshipTasksCount() {
        return workmanshipTasksCount;
    }

    public void setWorkmanshipTasksCount(Integer workmanshipTasksCount) {
        this.workmanshipTasksCount = workmanshipTasksCount;
    }
}
