package com.tsd.workshop.transaction.media;

import com.tsd.workshop.ErrorCodedRuntimeException;

public class WrongMediaOwnerException extends ErrorCodedRuntimeException {

    public WrongMediaOwnerException(Long mediaId, Long wrongServiceId) {
        super("media uploaded (id: %s) don't belong to service id: %s".formatted(mediaId, wrongServiceId));
    }

    @Override
    public String errorCode() {
        return "MEDIA-001";
    }
}
