package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestScenariosRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.TestScenariosService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    @GetMapping(EndpointURI.TEST_SCENARIO_BY_PROJECT_ID)
    public ResponseEntity<Object> getAllTestScenariosByProjectIdWithPagination(@PathVariable Long id,
                                                                               @RequestParam(name = "page") int page,
                                                                               @RequestParam(name = "size") int size, @RequestParam(name = "direction") String direction,
                                                                               @RequestParam(name = "sortField") String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
        if (!testScenariosService.existsByTestScenarioId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getGetTestScenarioNotHaveProjectId()));
        }
        return ResponseEntity.ok(new PaginatedContentResponse<>(Constants.TESTSCENARIOS, testScenariosService.getAllTestScenariosByProjectIdWithPagination(id, pageable, pagination),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllTestScenarioSuccessGivenProjectId(), pagination));
    }

    @DeleteMapping(value = EndpointURI.TEST_SCENARIO_BY_ID)
    public ResponseEntity<Object> DeleteTestScenarioById(@PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (!testScenariosService.existsByTestScenarioId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenarioNotExistCode(), statusCodeBundle.getTestScenarioNotExistsMessage()));
        }
        testScenariosService.DeleteTestScenariosById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteTestScenarioSuccessMessage()));
    }

}
