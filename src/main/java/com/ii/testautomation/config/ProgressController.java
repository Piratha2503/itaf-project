package com.ii.testautomation.config;

import com.ii.testautomation.service.TestGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class ProgressController {
    @Autowired
    private TestGroupingService testGroupingService;

    @RequestMapping(value="/calculateProgressPercentage")
    public ResponseEntity<Integer> getProgressBarPercentage() {
        int percentage = testGroupingService.calculatePercentage();
        return ResponseEntity.ok(percentage);
    }

}
