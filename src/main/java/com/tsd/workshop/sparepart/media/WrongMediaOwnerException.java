package com.tsd.workshop.sparepart.media;

import com.tsd.workshop.ErrorCodedRuntimeException;

public class WrongMediaOwnerException extends ErrorCodedRuntimeException {

    public WrongMediaOwnerException(Long mediaId, Long wrongSparePartId) {
        super("media uploaded (id: %s) don't belong to spare part id: %s".formatted(mediaId, wrongSparePartId));
    }

    @Override
    public String errorCode() {
        return "MEDIA-002";
    }
}
