package com.tsd.workshop.sparepart.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsd.workshop.r2dbc.AbstractJsonToDomainReadConverter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;

@ReadingConverter
public class JsonToOemsReadConverter extends AbstractJsonToDomainReadConverter<List<OEM>> {

    public JsonToOemsReadConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}
