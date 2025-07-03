package com.tsd.workshop.r2dbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.TypeMismatchDataAccessException;

public abstract class AbstractDomainToJsonWriteConverter<T> implements Converter<T, Json> {

    private final ObjectMapper objectMapper;

    private final String defaultValueIfNull;

    public AbstractDomainToJsonWriteConverter(ObjectMapper objectMapper, String defaultValueIfNull) {
        this.objectMapper = objectMapper;
        this.defaultValueIfNull = defaultValueIfNull;
    }

    @Override
    public Json convert(T source) {
        if (source == null) {
            return Json.of(defaultValueIfNull);
        }
        try {
            return Json.of(objectMapper.writeValueAsBytes(source));
        }
        catch (JsonProcessingException ex) {
            throw new TypeMismatchDataAccessException("failed to convert %s to proper json".formatted(source), ex);
        }
    }
}
