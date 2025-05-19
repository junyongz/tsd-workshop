package com.tsd.workshop.transaction.data.sql;

import java.util.List;
import java.util.stream.Collectors;

public class ItemDescKeywords {
    private final List<String> keywords;

    ItemDescKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public static ItemDescKeywords of(List<String> keywords) {
        return new ItemDescKeywords(keywords);
    }

    public String toSql() {
        return String.format("(%s)", keywords.stream()
                .map("upper(item_description) like upper('%%%s%%')"::formatted)
                .collect(Collectors.joining(" or ")));
    }
}
