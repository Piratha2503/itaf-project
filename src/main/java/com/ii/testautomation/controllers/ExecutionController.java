package com.ii.testautomation.controllers;


import com.ii.testautomation.service.TestGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ExecutionController {
    @Autowired
    private TestGroupingService testGroupingService;

    public ResponseEntity
}
