package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.entities.TestScenarios;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.TestScenariosService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class TestScenariosController {
    @Autowired
    private TestScenariosService testScenariosService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.TEST_SCENARIO)
    public ResponseEntity<Object> insertTestScenario(@RequestBody TestScenariosRequest testScenariosRequest) {
        if (testScenariosService.existsByTestScenarioNameIgnoreCase(testScenariosRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosAlreadyExistCode(), statusCodeBundle.getTestScenariosNameAlreadyExistMessage()));
        else if (testScenariosService.existByTestCaseList(testScenariosRequest))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosAlreadyExistCode(), statusCodeBundle.getTestCasesListAlreadyExistMessage()));
        else {
            testScenariosService.saveTestScenario(testScenariosRequest);
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getTestScenariosSaveMessage()));
        }
    }

    @PutMapping(value = EndpointURI.TEST_SCENARIO_UPDATE_EXECUTION_STATUS)
    public ResponseEntity<Object> updateTestScenarioStatus(@PathVariable Long id, @PathVariable Long projectId) {
        if (!projectService.existByProjectId(projectId)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (!testScenariosService.existsByTestScenarioId(id)
        ) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosNotExistCode(), "testScenarioNotExists"));
        }
        if(!projectService.hasJarPath(projectId))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getProjectJarPathNotProvideMessage()));
        }
        if(!projectService.hasConfigPath(projectId))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getProjectConfigPathNotProvideMessage()));
        }
        if(!testScenariosService.hasExcelPath(id))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getExcelPathNotProvideMessage()));
        }
        testScenariosService.updateExecutionStatus(id,projectId);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getExecutionSuccessMessage()));

    }
}
