package com.tsd.workshop.sparepart.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Keywords {

    public static String toSql(List<String> keywords) {
        return IntStream.range(0, keywords.size()).mapToObj( i -> """
            lower(part_no) like %s
            or lower(part_name) like %s
            or lower(description) like %s
            or exists (select 1
                     from json_array_elements(compatible_trucks) AS truck
                           WHERE lower(truck->>'make') like %s)
            or exists (select 1
                     from json_array_elements(compatible_trucks) AS truck
                           WHERE lower(truck->>'model') like %s)
            or exists (select 1
                     from json_array_elements(compatible_trucks) AS truck
                           WHERE lower(truck->>'make') || ' ' || lower(truck->>'model')  like %s)
            """.formatted(":keyword_" + i, ":keyword_" + i, ":keyword_" + i, ":keyword_" + i, ":keyword_" + i, ":keyword_" + i))
                .collect(Collectors.joining(" or "));
    }

    public static Map<String, String> toBindValues(List<String> keywords) {
        Map<String, String> values = new HashMap<>();
        for (int i=0; i<keywords.size(); i++) {
            values.put("keyword_" +i, "%%%s%%".formatted(keywords.get(i).toLowerCase()));
        }
        return values;
    }
}
