package com.tsd.workshop.sparepart.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsd.workshop.r2dbc.AbstractDomainToJsonWriteConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.List;

@WritingConverter
public class OemsToJsonWriteConverter extends AbstractDomainToJsonWriteConverter<List<OEM>> {

    public OemsToJsonWriteConverter(ObjectMapper objectMapper) {
        super(objectMapper, "[]");
    }
}
