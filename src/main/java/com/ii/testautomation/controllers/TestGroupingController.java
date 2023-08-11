package com.ii.testautomation.controllers;


import com.ii.testautomation.dto.request.TestGroupingRequest;

import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.utils.Constants;

import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.TestCasesService;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.service.TestTypesService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TestGroupingController {
    @Autowired
    private TestGroupingService testGroupingService;
    @Autowired
    private TestTypesService testTypesService;
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.TEST_GROUPING)
    public ResponseEntity<Object> saveTestGrouping(@RequestBody TestGroupingRequest testGroupingRequest) {
        if (!testTypesService.existsByTestTypesId(testGroupingRequest.getTestTypeId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestTypesNotExistCode(), statusCodeBundle.getTestTypesNotExistsMessage()));
        }

        for (Long testCaseId : testGroupingRequest.getTestCaseId()) {
            if (!testCasesService.existsByTestCasesId(testCaseId)) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesNotExistCode(), statusCodeBundle.getTestCasesNotExistsMessage()));
            }
        }
        if (!testGroupingService.allTestCasesInSameProject(testGroupingRequest.getTestCaseId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getTestCasesAndProjectNotExistsSameProjectMessage()));
        }
        Long testcaseId = null;
        for (Long testCaseId : testGroupingRequest.getTestCaseId()) {
            testcaseId = testCaseId;
            break;
        }
        if (testGroupingService.existsByTestGroupingName(testGroupingRequest.getName(), testcaseId)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingAlReadyExistCode(), statusCodeBundle.getTestGroupingNameAlReadyExistMessage()));
        }
        testGroupingService.saveTestGrouping(testGroupingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestGroupingSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.TEST_GROUPING)
    public ResponseEntity<Object> editTestGrouping(@RequestBody TestGroupingRequest testGroupingRequest) {
        if (!testGroupingService.existsByTestGroupingId(testGroupingRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        if (!testTypesService.existsByTestTypesId(testGroupingRequest.getTestTypeId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypesNotExistCode(),
                    statusCodeBundle.getTestTypesNotExistsMessage()));
        }
        for (Long testCaseId : testGroupingRequest.getTestCaseId()) {
            if (!testCasesService.existsByTestCasesId(testCaseId)) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesNotExistCode(), statusCodeBundle.getTestCasesNotExistsMessage()));
            }
        }
        if (!testGroupingService.allTestCasesInSameProject(testGroupingRequest.getTestCaseId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getTestCasesAndProjectNotExistsSameProjectMessage()));
        }
        Long testcaseId = null;
        for (Long testCaseId : testGroupingRequest.getTestCaseId()) {
            testcaseId = testCaseId;
            break;
        }
        if (testGroupingService.isUpdateTestGroupingNameExits(testGroupingRequest.getName(), testcaseId, testGroupingRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingAlReadyExistCode(), statusCodeBundle.getTestGroupingNameAlReadyExistMessage()));
        }
        testGroupingService.saveTestGrouping(testGroupingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateTestGroupingSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.TEST_GROUPING_UPDATE_EXECUTION_STATUS)
    public ResponseEntity<Object> updateExecution(@PathVariable Long id, @PathVariable Long projectId) {
        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        if(!projectService.existByProjectId(projectId))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getProjectNotExistCode(),statusCodeBundle.getProjectNotExistsMessage()
                    ));
        }
        testGroupingService.updateTestGroupingExecutionStatus(id, projectId);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING)
    public ResponseEntity<Object> getAllWithMultiSearch(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "direction") String direction, @RequestParam(name = "sortField") String sortField, TestGroupingSearch testGroupingSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPINGS, testGroupingService.multiSearchTestGrouping(pageable, pagination, testGroupingSearch), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetAllTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_BY_TEST_TYPE_ID)
    public ResponseEntity<BaseResponse> getAllTestGroupingByTestTypeId(@PathVariable Long id) {
        if (!testTypesService.existsByTestTypesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeNotExistCode(),
                    statusCodeBundle.getTestTypeNotExistMessage()));
        }
        List<TestGroupingResponse> testGroupingResponseList = testGroupingService.getAllTestGroupingByTestTypeId(id);
        if (testGroupingResponseList.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getTestGroupingFileEmptyMessage()));

        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPINGS, testGroupingResponseList, RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getTestGroupingByTestType()));
    }

    @DeleteMapping(value = EndpointURI.TEST_GROUPING_BY_ID)
    public ResponseEntity<Object> deleteTestGroupingById(@PathVariable Long id) {
        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingNotExistCode(), statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        testGroupingService.deleteTestGroupingById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_BY_ID)
    public ResponseEntity<Object> getTestGroupingById(@PathVariable Long id) {
        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingNotExistCode(), statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPING, testGroupingService.getTestGroupingById(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_BY_TEST_CASE_ID)
    public ResponseEntity<Object> getTestGroupingByTestCaseId(@PathVariable Long id) {
        if (!testCasesService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCasesNotExistCode(),
                    statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        if (!testGroupingService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getGetTestGroupingNotHaveTestCaseId()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPING, testGroupingService.getAllTestGroupingByTestCaseId(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestGroupingSuccessMessage()));

    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_BY_PROJECT_ID)
    public ResponseEntity<Object> getTestGroupingByProjectId(@PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (!testGroupingService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getGetTestGroupingNotHaveProjectId()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPING, testGroupingService.getAllTestGroupingByProjectId(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestGroupingSuccessMessage()));
    }


}
