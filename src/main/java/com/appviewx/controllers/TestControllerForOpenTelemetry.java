package com.appviewx.controllers;

import com.appviewx.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/test")
public class TestControllerForOpenTelemetry {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestControllerForOpenTelemetry.class);

    @Autowired
    public TestControllerForOpenTelemetry() {
    }

    @PostMapping("/testGet")
    public String testGet(@Valid @RequestBody Student student) {
        LOGGER.info("in testGet() method.");
        return "OK";
    }

    @GetMapping("/user")
    public String testUser() {
        LOGGER.info("in testUser() method.");
        return "Test User";
    }

    @GetMapping("/admin")
    public String testAdmin() {
        LOGGER.info("in testAdmin() method.");
        return "Test Admin";
    }

}
