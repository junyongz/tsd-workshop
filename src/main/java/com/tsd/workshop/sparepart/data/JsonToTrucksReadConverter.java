package com.tsd.workshop.sparepart.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsd.workshop.r2dbc.AbstractJsonToDomainReadConverter;

import java.util.List;

public class JsonToTrucksReadConverter extends AbstractJsonToDomainReadConverter<List<Truck>> {
    public JsonToTrucksReadConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
