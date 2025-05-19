package com.tsd.workshop.transaction.data.sql;

import java.util.List;
import java.util.stream.Collectors;

public class PartNameKeywords {

    private final List<String> keywords;

    PartNameKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public static PartNameKeywords of(List<String> keywords) {
        return new PartNameKeywords(keywords);
    }

    public String toSql() {
        return String.format("(%s)", keywords.stream()
                .map("upper(coalesce(mssp.item_code,'') || ' ' || mssp.part_name) like upper('%%%s%%')"::formatted)
                .collect(Collectors.joining(" or ")));
    }
}
