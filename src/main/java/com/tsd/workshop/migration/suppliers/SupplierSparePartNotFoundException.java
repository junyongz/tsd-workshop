package com.tsd.workshop.migration.suppliers;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.migration.suppliers.data.SupplierSparePart;

public class SupplierSparePartNotFoundException extends ErrorCodedRuntimeException {

    public SupplierSparePartNotFoundException(SupplierSparePart spp) {
        super("there is no supplier spare part found for %s".formatted(spp));
    }

    @Override
    public String errorCode() {
        return "SPP-001";
    }
}
