package com.tsd.workshop.transaction.data.sql;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VehicleNoKeywords {

    private final List<String> keywords;

    VehicleNoKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public static VehicleNoKeywords of(List<String> keywords) {
        return new VehicleNoKeywords(keywords);
    }

    public String toSql() {
        return "AND vehicle_no in (%s)"
                .formatted(IntStream.range(0, keywords.size())
                        .mapToObj(v -> ":k" + v)
                        .collect(Collectors.joining(",")));
    }

}
