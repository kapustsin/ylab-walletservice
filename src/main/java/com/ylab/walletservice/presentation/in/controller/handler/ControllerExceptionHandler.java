package com.ylab.walletservice.presentation.in.controller.handler;

import com.ylab.walletservice.service.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LogService.add(e.getMessage());
        return new ResponseEntity<>("Internal server error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}