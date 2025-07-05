package com.tsd.workshop.web;

import org.springframework.http.HttpHeaders;

public abstract class ResponsePaginationUtils {

    public static void setHeaders(HttpHeaders headers, Long count, int pageNumber, int pageSize) {
        headers.add("X-Total-Elements", String.valueOf(count));
        headers.add("X-Total-Pages", String.valueOf((long) Math.ceil((double) count / pageSize)));
        headers.add("X-Current-Page", String.valueOf(pageNumber));
        headers.add("X-Page-Size", String.valueOf(pageSize));
    }

}