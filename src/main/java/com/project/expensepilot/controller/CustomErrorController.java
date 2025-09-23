package com.project.expensepilot.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");

        String message = "An error occurred";
        if (statusCode != null) {
            if (statusCode == 404) {
                message = "Page not found - ExpensePilot API is running. Try /api/ for API endpoints.";
            } else if (statusCode == 500) {
                message = "Internal server error";
            } else {
                message = "Error " + statusCode + " occurred";
            }
        }

        return new ResponseEntity<>(message, HttpStatus.valueOf(statusCode != null ? statusCode : 500));
    }
}
