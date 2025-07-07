package com.tsd.workshop.sql;

public class Pagination {

    public static String toSql(int pageNumber, int pageSize) {
        if (pageNumber > 0 && pageSize > 0) {
            return "limit %s offset %s".formatted(pageSize, (pageNumber-1) * pageSize);
        }
        return "";
    }
}
