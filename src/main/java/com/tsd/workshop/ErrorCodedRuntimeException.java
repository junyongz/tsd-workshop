package com.tsd.workshop;

import org.springframework.core.NestedRuntimeException;

public abstract class ErrorCodedRuntimeException extends NestedRuntimeException  {

    public ErrorCodedRuntimeException(String message) {
        super(message);
    }

    public abstract String errorCode();
}
