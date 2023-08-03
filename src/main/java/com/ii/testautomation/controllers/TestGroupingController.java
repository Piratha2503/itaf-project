package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.TestCasesService;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.service.TestTypesService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getTestGroupingAlReadyExistCode(), statusCodeBundle.getTestGroupingNameAlReadyExistMessage()));
        }
        testGroupingService.saveTestGrouping(testGroupingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestGroupingSuccessMessage()));
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
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getTestGroupingAlReadyExistCode(), statusCodeBundle.getTestGroupingNameAlReadyExistMessage()));
        }
        testGroupingService.saveTestGrouping(testGroupingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveTestGroupingSuccessMessage()));
    }


    @DeleteMapping(value = EndpointURI.TEST_GROUPING_BY_ID)
    public ResponseEntity<Object> deleteTestGroupingById(@PathVariable Long id) {
        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingNotExistCode(), statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        testGroupingService.deleteTestGroupingById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteTestGroupingSuccessMessage()));

    }

}
