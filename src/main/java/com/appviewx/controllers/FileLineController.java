package com.appviewx.controllers;

import com.appviewx.services.FileReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@RestController
public class FileLineController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLineController.class);

    @Autowired
    FileReaderService fileReaderService;

    @PostMapping("/readLinesAndSaveUsingJDBC")
    public void readLinesAndSaveUsingJDBC(@RequestHeader Map<String,String> headers, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("in readLinesAndSaveUsingJDBC() method.");

        long startTime = System.currentTimeMillis();
        fileReaderService.readDataFromFile("/home/satya/FragmaDocuments/Mashreq/test-genfile.csv");
        LOGGER.info("FInished Reading file");
    }

    /*@PostMapping("/readLinesAndSaveUsingJDBC")
    public void readLinesAndSaveUsingJDBC(@RequestHeader Map<String,String> headers, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("in readLinesAndSaveUsingJDBC() method.");

        long startTime = System.currentTimeMillis();
        fileReaderService.readDataFromFile("/home/satya/FragmaDocuments/Mashreq/test-genfile.csv");
        LOGGER.info("FInished Reading file");
    }*/


}
