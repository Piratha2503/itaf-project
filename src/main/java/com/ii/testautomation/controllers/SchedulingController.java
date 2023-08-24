package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.SchedulingRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.*;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private TestScenariosService testScenariosService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private TestGroupingService testGroupingService;


    @PostMapping(value = EndpointURI.TEST_SCHEDULING)
    public ResponseEntity<Object> saveScheduling(@RequestBody SchedulingRequest schedulingRequest) {
        if (!projectService.existByProjectId(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
        }
        for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestScenario().entrySet()) {
            if (!testScenariosService.existsByTestScenarioId(entry.getValue())) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosNotExistCode(), "testScenarioNotExists"));
            }
        }
        for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestCase().entrySet()) {
            if (!testCasesService.existsByTestCasesId(entry.getValue())) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosNotExistCode(), "testScenarioNotExists"));
            }
        }
        if (!projectService.hasJarPath(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getProjectJarPathNotProvideMessage()));
        }
        if (!projectService.hasConfigPath(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getProjectConfigPathNotProvideMessage()));
        }
        if (!testGroupingService.hasExcelPath(schedulingRequest.getGroupId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getExcelPathNotProvideMessage()));
        }

        schedulingService.saveTestScheduling(schedulingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestSchedulingSuccessMessage()));
    }


    public ResponseEntity<Object> Execution() {
        schedulingService.autoScheduling();
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getExecutionSuccessMessage()));
    }
}