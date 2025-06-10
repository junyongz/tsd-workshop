package com.tsd.workshop.workmanship;

import com.tsd.workshop.ErrorCodedRuntimeException;
import com.tsd.workshop.workmanship.data.WorkmanshipTask;

public class WrongWorkmanshipOwnerException extends ErrorCodedRuntimeException {

    public WrongWorkmanshipOwnerException(WorkmanshipTask task, Long wrongServiceId) {
        super("Task %s is not belong to: %s ".formatted(task, wrongServiceId));
    }

    @Override
    public String errorCode() {
        return "TASK-001";
    }
}
