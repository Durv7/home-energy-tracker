package com.dsk.device_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DeviceNotFoundException.class)
    public ProblemDetail handleDeviceNotFound(DeviceNotFoundException deviceNotFoundException, HttpServletRequest httpRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(deviceNotFoundException.getMessage());
        problemDetail.setInstance(URI.create(httpRequest.getRequestURI()));

        return problemDetail;
    }
}
