package com.tsd.workshop.transaction.utilization;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;

public class QuantityNotMatchedException extends ErrorCodedRuntimeException {

    private SupplierSparePart ssp;

    private int usage;

    public QuantityNotMatchedException(SupplierSparePart ssp, int usage) {
        super("Quantity for spare part %s should not be changed after creation and used, currently %s in use".formatted(ssp, usage));
        this.ssp = ssp;
        this.usage = usage;
    }

    public SupplierSparePart getSsp() {
        return ssp;
    }

    public int getUsage() {
        return usage;
    }

    @Override
    public String errorCode() {
        return "SP-QUANTITY-002";
    }
}
