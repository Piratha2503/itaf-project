package com.ii.testautomation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.entities.TestScenarios;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.TestScenariosService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TestScenariosController {
    @Autowired
    private TestScenariosService testScenariosService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.TEST_SCENARIO)
    public ResponseEntity<Object> insertTestScenario(@RequestParam(name = "name",required = false) String name,
                                                     @RequestParam(name = "moduleIds",required = false) List<Long> moduleIds,
                                                     @RequestParam(name = "mainModuleIds",required = false) List<Long> mainModuleIds,
                                                     @RequestParam(name = "subModuleIds",required = false) List<Long> subModuleIds,
                                                     @RequestParam(name = "testCasesId",required = false) List<Long> testCasesId
                                                     ) throws JsonProcessingException{
        TestScenariosRequest testScenariosRequest = new TestScenariosRequest();
        testScenariosRequest.setName(name);
        if (moduleIds == null) testScenariosRequest.setModuleIds(null);
        testScenariosRequest.setModuleIds(moduleIds);
        if (mainModuleIds == null) testScenariosRequest.setMainModuleIds(null);
        testScenariosRequest.setMainModuleIds(mainModuleIds);
        if (subModuleIds == null) testScenariosRequest.setSubModuleIds(null);
        testScenariosRequest.setSubModuleIds(subModuleIds);
        if (testCasesId == null) testScenariosRequest.setTestCasesId(null);
        testScenariosRequest.setTestCasesId(testCasesId);

      if (testScenariosService.existsByTestScenarioNameIgnoreCase(testScenariosRequest.getName()))
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosAlreadyExistCode(),statusCodeBundle.getTestScenariosNameAlreadyExistMessage()));

      else if (!(testScenariosRequest.getTestCasesId()==null) && testScenariosService.existByTestCaseList(testScenariosRequest))
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosAlreadyExistCode(),statusCodeBundle.getTestCasesListAlreadyExistMessage()));
        else {
            testScenariosService.saveTestScenario(testScenariosRequest);
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getTestScenariosSaveMessage()));
        }

    }
}
