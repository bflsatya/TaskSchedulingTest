package com.appviewx.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleInvalidMethodArguments(MethodArgumentNotValidException ex) {
        Map<String,Object> respBody = new LinkedHashMap<>();
        respBody.put("timestamp", new Date());
        respBody.put("status", HttpStatus.BAD_REQUEST.value());
        Map<String, String> fieldNameAndValidationErrors = ex.getBindingResult().getFieldErrors()
                                                                            .stream()
                                                                            .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
        respBody.put("fieldValidationErrors",fieldNameAndValidationErrors);
        return new ResponseEntity<>(respBody,HttpStatus.BAD_REQUEST);
    }

    // If we want to implement Custom Response Handling using ResponseEntityExceptionHandler
    // then, remove the extends comment above and uncomment the method below
    /*@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,Object> respBody = new LinkedHashMap<>();
        respBody.put("timestamp", new Date());
        respBody.put("status", status.value());
        Map<String, String> fieldNameAndValidationErrors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
        respBody.put("fieldValidationErrors",fieldNameAndValidationErrors);
        return new ResponseEntity<>(respBody,status);
    }*/
}
