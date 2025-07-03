package com.tsd.workshop.r2dbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.TypeMismatchDataAccessException;

public abstract class AbstractJsonToDomainReadConverter<T> implements Converter<Json, T> {

    private final ObjectMapper objectMapper;

    public AbstractJsonToDomainReadConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public T convert(Json source) {
        try {
            return objectMapper.readValue(source.asString(), new TypeReference<>() {});
        }
        catch (JsonProcessingException ex) {
            throw new TypeMismatchDataAccessException("failed to read json", ex);
        }
    }
}
