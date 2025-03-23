package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.ErrorCodedRuntimeException;

public class QuantityOverflowException extends ErrorCodedRuntimeException {

    private int usageQuantity;

    private int orderQuantity;

    public QuantityOverflowException(Integer usageQuantity, Integer orderQuantity) {
        super("There is only %s, but total needed %s, please purchase again".formatted(orderQuantity, usageQuantity));
        this.usageQuantity = usageQuantity;
        this.orderQuantity = orderQuantity;
    }

    public int getUsageQuantity() {
        return usageQuantity;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    @Override
    public String errorCode() {
        return "SP-QUANTITY-001";
    }
}
