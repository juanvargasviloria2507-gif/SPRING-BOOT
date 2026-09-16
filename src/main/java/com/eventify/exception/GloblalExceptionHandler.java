package com.eventify.exception;


import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;


@RestControllerAdvice
public class GloblalExceptionHandler {

    @ExceptionHandler({EmptyException.class, DuplicateException.class})

    public ResponseEntity<Object> handleException(Exception ex){

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
