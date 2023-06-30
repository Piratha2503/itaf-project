package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
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
        if (testGroupingService.existsByTestGroupingName(testGroupingRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingAlReadyExistCode(),
                    statusCodeBundle.getTestGroupingNameAlReadyExistMessage()));
        }
        if (!testCasesService.existsByTestCasesId(testGroupingRequest.getTestCaseId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCasesNotExistCode(),
                    statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        if (!testTypesService.existsByTestTypesId(testGroupingRequest.getTestTypeId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypesNotExistCode(),
                    statusCodeBundle.getTestTypesNotExistsMessage()));
        }
        testGroupingService.saveTestGrouping(testGroupingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveTestGroupingSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.TEST_GROUPING)
    public ResponseEntity<Object> editTestGrouping(@RequestBody TestGroupingRequest testGroupingRequest) {
        if (!testGroupingService.existsByTestGroupingId(testGroupingRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        if (!testCasesService.existsByTestCasesId(testGroupingRequest.getTestCaseId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCasesNotExistCode(),
                    statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        if (!testTypesService.existsByTestTypesId(testGroupingRequest.getTestTypeId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypesNotExistCode(),
                    statusCodeBundle.getTestTypesNotExistsMessage()));
        }
        if (testGroupingService.isUpdateTestGroupingNameExits(testGroupingRequest.getName(), testGroupingRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingAlReadyExistCode(),
                    statusCodeBundle.getTestGroupingNameAlReadyExistMessage()));
        }
        testGroupingService.saveTestGrouping(testGroupingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_BY_ID)
    public ResponseEntity<Object> getTestGroupingById(@PathVariable Long id) {
        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPINGS, testGroupingService.getTestGroupingById(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetTestGroupingSuccessMessage()
        ));
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
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPINGS,
                testGroupingService.getALlTestGroupingByTestCaseId(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_BY_TEST_TYPE_ID)
    public ResponseEntity<Object> getTestGroupingByTestTypeId(@PathVariable Long id) {
        if (!testTypesService.existsByTestTypesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypesNotExistCode(),
                    statusCodeBundle.getTestTypesNotExistsMessage()));
        }
        if (!testGroupingService.existsByTestTypesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getGetTestGroupingNotHaveTestTypeId()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPINGS,
                testGroupingService.getALlTestGroupingByTestCaseId(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllTestGroupingSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TEST_GROUPING_SEARCH)
    public ResponseEntity<Object> getALLTestGroupingWithMultiSearch(@RequestParam(name = "page") int page,
                                                                    @RequestParam(name = "size") int size,
                                                                    @RequestParam(name = "direction") String direction,
                                                                    @RequestParam(name = "sortField") String sortField,
                                                                    TestGroupingSearch testGroupingSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.TEST_GROUPINGS, testGroupingService.multiSearchTestGrouping(pageable,
                pagination, testGroupingSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllTestGroupingSuccessMessage()));
    }

    @DeleteMapping(value = EndpointURI.TEST_GROUPING_BY_ID)
    public ResponseEntity<Object> deleteTestGroupingById(@PathVariable Long id) {
        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        testGroupingService.deleteTestGroupingById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getDeleteTestGroupingSuccessMessage()));

    }
}
