package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.ErrorCodedRuntimeException;

import java.math.BigDecimal;

public class QuantityOverflowException extends ErrorCodedRuntimeException {

    private BigDecimal usageQuantity;

    private BigDecimal orderQuantity;

    public QuantityOverflowException(BigDecimal usageQuantity, BigDecimal orderQuantity) {
        super("There is only %s purchased, but total needed: %s, please purchase again".formatted(orderQuantity, usageQuantity));
        this.usageQuantity = usageQuantity;
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getUsageQuantity() {
        return usageQuantity;
    }

    public BigDecimal getOrderQuantity() {
        return orderQuantity;
    }

    @Override
    public String errorCode() {
        return "SP-QUANTITY-001";
    }
}
