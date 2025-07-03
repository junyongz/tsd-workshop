package com.tsd.workshop.vehicle.fleet;

import com.tsd.workshop.telematics.maps.render.Coordination;

import java.time.LocalDateTime;

public class FleetInfo {

    private String vehicleNo;

    private LocalDateTime recordedDateTime;

    private Integer mileageKm;

    private Double remainingFuelLitre;

    private Coordination coordination;

    public FleetInfo(String vehicleNo, LocalDateTime recordedDateTime, Integer mileageKm, Double remainingFuelLitre, Coordination coordination) {
        this.vehicleNo = vehicleNo;
        this.recordedDateTime = recordedDateTime;
        this.mileageKm = mileageKm;
        this.remainingFuelLitre = remainingFuelLitre;
        this.coordination = coordination;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public LocalDateTime getRecordedDateTime() {
        return recordedDateTime;
    }

    public void setRecordedDateTime(LocalDateTime recordedDateTime) {
        this.recordedDateTime = recordedDateTime;
    }

    public Integer getMileageKm() {
        return mileageKm;
    }

    public void setMileageKm(Integer mileageKm) {
        this.mileageKm = mileageKm;
    }

    public Double getRemainingFuelLitre() {
        return remainingFuelLitre;
    }

    public void setRemainingFuelLitre(Double remainingFuelLitre) {
        this.remainingFuelLitre = remainingFuelLitre;
    }

    public Coordination getCoordination() {
        return coordination;
    }

    public void setCoordination(Coordination coordination) {
        this.coordination = coordination;
    }

    public String asJson() {
        return "{" +
                "\"vehicleNo\": \"" + vehicleNo + "\"" +
                ", \"recordedDateTime\": \"" + recordedDateTime + "\"" +
                ", \"mileageKm\": " + mileageKm +
                ", \"remainingFuelLitre\": " + remainingFuelLitre +
                ", \"coordination\": " + coordination.asJson() +
                "}";
    }

    @Override
    public String toString() {
        return "FleetInfo{" +
                "vehicleNo='" + vehicleNo + "'" +
                ", recordedDateTime=" + recordedDateTime +
                ", mileageKm=" + mileageKm +
                ", remainingFuelLitre=" + remainingFuelLitre +
                ", coordination=" + coordination +
                '}';
    }
}
