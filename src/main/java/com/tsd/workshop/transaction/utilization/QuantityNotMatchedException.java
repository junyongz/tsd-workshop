package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;

import java.math.BigDecimal;

public class QuantityNotMatchedException extends ErrorCodedRuntimeException {

    private SupplierSparePart ssp;

    private BigDecimal usage;

    public QuantityNotMatchedException(SupplierSparePart ssp, BigDecimal usage) {
        super("Quantity for spare part %s should not be changed after creation and used, currently %s in use".formatted(ssp, usage));
        this.ssp = ssp;
        this.usage = usage;
    }

    public SupplierSparePart getSsp() {
        return ssp;
    }

    public BigDecimal getUsage() {
        return usage;
    }

    @Override
    public String errorCode() {
        return "SP-QUANTITY-002";
    }
}
