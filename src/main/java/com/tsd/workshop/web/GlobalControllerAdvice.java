package com.tsd.workshop.web;

import com.tsd.workshop.ErrorCodedRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler
    public ResponseEntity<String> handleAnyProblem(ErrorCodedRuntimeException ex) {
        logger.error("hit some coded error", ex);

        return new ResponseEntity<>(
                """
                {"code": "%s", "description": "%s"}
                """.formatted(ex.errorCode(), ex.getLocalizedMessage()),
                MultiValueMap.fromSingleValue(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)),
                HttpStatus.BAD_REQUEST);
    }

}
