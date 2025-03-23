package com.tsd.workshop.web;

import com.tsd.workshop.ErrorCodedRuntimeException;
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

    @ExceptionHandler
    public ResponseEntity<String> handleAnyProblem(ErrorCodedRuntimeException ex) {
        return new ResponseEntity<>(
                """
                {"code": "%s", "description": "%s"}
                """.formatted(ex.errorCode(), ex.getLocalizedMessage()),
                MultiValueMap.fromSingleValue(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)),
                HttpStatus.BAD_REQUEST);
    }

}
