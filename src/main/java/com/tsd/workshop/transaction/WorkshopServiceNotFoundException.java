package com.tsd.workshop.transaction;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.transaction.data.WorkshopService;

public class WorkshopServiceNotFoundException extends ErrorCodedRuntimeException {

    public WorkshopServiceNotFoundException(WorkshopService ws) {
        super("there is no workshop %s found, are you nuts?".formatted(ws));
    }

    @Override
    public String errorCode() {
        return "WS-001";
    }
}
