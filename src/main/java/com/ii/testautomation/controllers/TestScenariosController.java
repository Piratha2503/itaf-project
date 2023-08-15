package com.ii.testautomation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.entities.TestCases;
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
    public ResponseEntity<Object> insertScenario(@RequestBody TestScenariosRequest testScenariosRequest) {
        if (testScenariosService.existsByTestScenarioNameIgnoreCase(testScenariosRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosAlreadyExistCode(), statusCodeBundle.getTestScenariosNameAlreadyExistMessage()));
        testScenariosService.saveTestScenario(testScenariosRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getTestScenariosSaveMessage()));
    }


    @PutMapping(EndpointURI.TEST_SCENARIO)
    public ResponseEntity<Object>UpdateTestScenarios(@RequestBody TestScenariosRequest testScenariosRequest){
      if (!testScenariosService.existsByTestScenarioId(testScenariosRequest.getId())){
          return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                  statusCodeBundle.getTestScenariosNotExistCode(),
                  statusCodeBundle.getTestScenariosIdNotExistMessage()));

      }

      if(testScenariosService.isUpdateTestScenariosNameExists(testScenariosRequest.getId(),testScenariosRequest.getName())){
          return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                  statusCodeBundle.getTestScenariosAlreadyExistCode(),
                  statusCodeBundle.getTestScenariosNameAlreadyExistMessage()));
      }
      testScenariosService.saveTestScenario(testScenariosRequest);
      return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
              statusCodeBundle.getCommonSuccessCode(),
              statusCodeBundle.getUpdateTestScenarioSuccessMessage()));
    }
}
