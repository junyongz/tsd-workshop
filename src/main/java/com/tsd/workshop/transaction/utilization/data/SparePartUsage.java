package com.tsd.workshop.transaction.utilization.data;

import com.tsd.workshop.transaction.data.WorkshopService;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("spare_part_usages")
public class SparePartUsage {
    @Id
    private Long id;
    private Long vehicleId;
    private String vehicleNo;
    private LocalDate usageDate;
    private Long orderId;
    private Long serviceId;
    private BigDecimal quantity;
    private BigDecimal soldPrice;

    // Default constructor
    public SparePartUsage() {}

    // Getters and Setters
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

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(BigDecimal soldPrice) {
        this.soldPrice = soldPrice;
    }

    // good to use for the first usage
    public WorkshopService toWorkshopService() {
        WorkshopService ws = new WorkshopService();
        ws.setVehicleId(this.vehicleId);
        ws.setVehicleNo(this.vehicleNo);
        ws.setStartDate(this.usageDate);
        return ws;
    }

    @Override
    public String toString() {
        return "SparePartUsage{" +
                "id=" + id +
                ", vehicleId=" + vehicleId +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", usageDate='" + usageDate + '\'' +
                ", orderId=" + orderId +
                ", serviceId=" + serviceId +
                ", quantity=" + quantity +
                ", soldPrice=" + soldPrice +
                '}';
    }
}