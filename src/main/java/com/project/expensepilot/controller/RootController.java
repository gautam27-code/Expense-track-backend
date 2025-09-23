package com.project.expensepilot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

     @GetMapping("/")
    public ResponseEntity<String> root() {
        return new ResponseEntity<>("ExpensePilot backend is running! API endpoints available at /api/", HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return new ResponseEntity<>("Server is healthy", HttpStatus.OK);
    }
}
