package com.tsd.workshop.r2dbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsd.workshop.sparepart.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomConfig {

    @Autowired
    private ObjectMapper objectMapper;

    // copied from R2dbcDataAutoConfiguration, to provide the custom converters
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(DatabaseClient databaseClient) {
        R2dbcDialect dialect = DialectResolver.getDialect(databaseClient.getConnectionFactory());

        List<Object> converters = new ArrayList<>(dialect.getConverters());
        converters.addAll(R2dbcCustomConversions.STORE_CONVERTERS);
        return new R2dbcCustomConversions(
                CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder(), converters),
                List.of(new JsonToOemsReadConverter(objectMapper),
                        new OemsToJsonWriteConverter(objectMapper),
                        new JsonToTrucksReadConverter(objectMapper),
                        new TrucksToJsonWriteConverter(objectMapper)));

    }
}
