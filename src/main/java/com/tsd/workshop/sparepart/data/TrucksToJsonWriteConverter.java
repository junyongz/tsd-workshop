package com.tsd.workshop.sparepart.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsd.workshop.r2dbc.AbstractDomainToJsonWriteConverter;

import java.util.List;

public class TrucksToJsonWriteConverter extends AbstractDomainToJsonWriteConverter<List<Truck>> {

    public TrucksToJsonWriteConverter(ObjectMapper objectMapper) {
        super(objectMapper, "[]");
    }
}
