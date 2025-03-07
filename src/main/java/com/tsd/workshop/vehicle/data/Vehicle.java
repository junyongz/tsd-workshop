package com.tsd.workshop.vehicle.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("vehicle")
public class Vehicle {
    @Id
    private Long id;
    @Column("plate_no")
    private String vehicleNo;
    @Column("trailer_no")
    private String trailerNo;
    @Column("company_id")
    private Long companyId;

    // Default constructor
    public Vehicle() {}

    // Parameterized constructor
    public Vehicle(String vehicleNo, String trailerNo) {
        this.vehicleNo = vehicleNo;
        this.trailerNo = trailerNo;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleNo='" + vehicleNo + '\'' +
                ", trailerNo='" + trailerNo + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}