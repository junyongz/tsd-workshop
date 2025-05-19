package com.tsd.workshop.transaction.data.sql;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleNoKeywords {

    private final List<String> keywords;

    VehicleNoKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public static VehicleNoKeywords of(List<String> keywords) {
        return new VehicleNoKeywords(keywords);
    }

    public String toSql() {
        return keywords.stream()
                .map("vehicle_no = '%s'"::formatted)
                .collect(Collectors.joining(" or "));
    }

}
