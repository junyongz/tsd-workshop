package com.tsd.workshop.vehicle.data;

import com.tsd.workshop.vehicle.VehicleStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("vehicle")
public class Vehicle {
    @Id
    private Long id;

    @InsertOnlyProperty
    @Column("plate_no")
    private String vehicleNo;
    private String trailerNo;
    private Long companyId;
    private LocalDate insuranceExpiryDate;
    private LocalDate roadTaxExpiryDate;
    private LocalDate inspectionDueDate;

    private VehicleStatus status;

    @ReadOnlyProperty
    private Integer latestMileageKm;

    // Default constructor
    public Vehicle() {}

    // Parameterized constructor
    public Vehicle(String vehicleNo, String trailerNo) {
        this.vehicleNo = vehicleNo;
        this.trailerNo = trailerNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTrailerNo() {
        return trailerNo;
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo = trailerNo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public LocalDate getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public LocalDate getRoadTaxExpiryDate() {
        return roadTaxExpiryDate;
    }

    public void setRoadTaxExpiryDate(LocalDate roadTaxExpiryDate) {
        this.roadTaxExpiryDate = roadTaxExpiryDate;
    }

    public LocalDate getInspectionDueDate() {
        return inspectionDueDate;
    }

    public void setInspectionDueDate(LocalDate inspectionDueDate) {
        this.inspectionDueDate = inspectionDueDate;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Integer getLatestMileageKm() {
        return latestMileageKm;
    }

    public void setLatestMileageKm(Integer latestMileageKm) {
        this.latestMileageKm = latestMileageKm;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleNo='" + vehicleNo + '\'' +
                ", trailerNo='" + trailerNo + '\'' +
                ", companyId=" + companyId +
                ", insuranceExpiryDate=" + insuranceExpiryDate +
                ", roadTaxExpiryDate=" + roadTaxExpiryDate +
                ", latestMileageKm=" + latestMileageKm +
                '}';
    }
}