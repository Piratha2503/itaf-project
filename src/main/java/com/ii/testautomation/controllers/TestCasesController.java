package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.SubModulesService;
import com.ii.testautomation.service.TestCasesService;
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
public class TestCasesController {
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private SubModulesService subModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.TESTCASE)
    public ResponseEntity<Object> saveTestCase(@RequestBody TestCaseRequest testCaseRequest) {
        if (!subModulesService.existsBySubModuleId(testCaseRequest.getSubModuleId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        if (testCasesService.existsByTestCasesName(testCaseRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCaseAlreadyExistsCode(), statusCodeBundle.getTestCaseNameAlreadyExistsMessage()));
        }
        testCasesService.saveTestCase(testCaseRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestCaseSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TESTCASE_BY_ID)
    public ResponseEntity<Object> GetTestcaseById(@PathVariable Long id) {
        if (!testCasesService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCasesNotExistCode(),
                    statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASE, testCasesService.getById(id), RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestCaseByIdSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.TESTCASE)
    public ResponseEntity<Object> UpdateTestCase(@RequestBody TestCaseRequest testCaseRequest) {
        if (!testCasesService.existsByTestCasesId(testCaseRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCasesNotExistCode(),
                    statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        if (testCasesService.isUpdateTestCaseNameExists(testCaseRequest.getId(), testCaseRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCaseAlreadyExistsCode(), statusCodeBundle.getTestCaseNameAlreadyExistsMessage()));
        }
        if (!subModulesService.existsBySubModuleId(testCaseRequest.getSubModuleId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        testCasesService.saveTestCase(testCaseRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateTestCaseSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TESTCASES)
    public ResponseEntity<Object> getAllWithMultiSearch(@RequestParam(name = "page") int page,
                                                        @RequestParam(name = "size") int size,
                                                        @RequestParam(name = "direction") String direction,
                                                        @RequestParam(name = "sortField") String sortField,
                                                        TestCaseSearch testCaseSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASES, testCasesService.multiSearchTestCase(pageable, pagination, testCaseSearch),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllTestCasesSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TESTCASES_BY_ID)
    public ResponseEntity<Object> getAllTestCaseBySubModuleId(@PathVariable Long id) {
        if (!subModulesService.existsBySubModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        List<TestCaseResponse> testCaseResponseList = testCasesService.getAllTestCaseBySubModuleId(id);
        if (testCaseResponseList.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getGetTestCaseNotHaveSubModuleIdMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASES, testCasesService.getAllTestCaseBySubModuleId(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestCaseBySubModuleIdSuccessMessage()));

    }

    @DeleteMapping(value = EndpointURI.TESTCASE_BY_ID)
    public ResponseEntity<Object> DeleteTestCaseById(@PathVariable Long id) {
        if (!testCasesService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestCasesNotExistCode(),
                    statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        testCasesService.DeleteTestCaseById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteTestCaseSuccessMessage()));
    }
}
