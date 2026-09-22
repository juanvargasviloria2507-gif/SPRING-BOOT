package com.eventify.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GloblalExceptionHandler {

    @ExceptionHandler({EmptyException.class, DuplicateException.class, FutureException.class})

    public ResponseEntity<Object> handleException(Exception ex){

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(NotFoundException.class)

    public ResponseEntity<Object> handleException(NotFoundException ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
