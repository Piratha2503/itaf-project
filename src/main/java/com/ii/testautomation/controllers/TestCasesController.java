package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.SubModulesService;
import com.ii.testautomation.service.TestCasesService;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import com.ii.testautomation.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class TestCasesController {
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private SubModulesService subModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private TestGroupingService testGroupingService;

    @PostMapping(value = EndpointURI.TESTCASE)
    public ResponseEntity<Object> saveTestCase(@RequestBody TestCaseRequest testCaseRequest) {
        if (!subModulesService.existsBySubModuleId(testCaseRequest.getSubModuleId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        if (testCasesService.existsByTestCasesName(testCaseRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCaseNameAlreadyExistsMessage(), statusCodeBundle.getTestCaseNameAlreadyExistsMessage()));
        }
        testCasesService.saveTestCase(testCaseRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestCaseSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TESTCASE_BY_ID)
    public ResponseEntity<Object> GetTestcaseById(@PathVariable Long id) {
        if (!testCasesService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesNotExistCode(), statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASE, testCasesService.getById(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestCaseByIdSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.TESTCASE)
    public ResponseEntity<Object> UpdateTestCase(@RequestBody TestCaseRequest testCaseRequest) {
        if (!testCasesService.existsByTestCasesId(testCaseRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesNotExistCode(), statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        if (testCasesService.isUpdateTestCaseNameExists(testCaseRequest.getId(), testCaseRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesAlreadyExistsCode(), statusCodeBundle.getTestCaseNameAlreadyExistsMessage()));
        }
        if (!subModulesService.existsBySubModuleId(testCaseRequest.getSubModuleId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        testCasesService.saveTestCase(testCaseRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getUpdateTestCaseSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.TESTCASES)
    public ResponseEntity<Object> getAllWithMultiSearch(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size, @RequestParam(name = "direction") String direction, @RequestParam(name = "sortField") String sortField, TestCaseSearch testCaseSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASES, testCasesService.multiSearchTestCase(pageable, pagination, testCaseSearch), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetAllTestCasesSuccessMessage()));
    }

    @PostMapping(EndpointURI.TESTCASE_IMPORT)
    public ResponseEntity<Object> testCaseImport(@RequestParam MultipartFile multipartFile) {
        Map<String, List<Integer>> errorMessages = new HashMap<>();
        List<TestCaseRequest> testCaseRequestList;
        try {
            if (multipartFile.getOriginalFilename().endsWith(".csv")) {
                if(!testCasesService.isCSVHeaderMatch(multipartFile)){
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                            statusCodeBundle.getFailureCode(),statusCodeBundle.getHeaderNotExistsMessage()));
                }else{
                    testCaseRequestList = testCasesService.csvToTestCaseRequest(multipartFile.getInputStream());
                }

            } else if (testCasesService.hasExcelFormat(multipartFile)) {
                if(!testCasesService.isExcelHeaderMatch(multipartFile)){
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                            statusCodeBundle.getFailureCode(),statusCodeBundle.getHeaderNotExistsMessage()));
                }else{
                    testCaseRequestList = testCasesService.excelToTestCaseRequest(multipartFile);
                }
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }
            for (int rowIndex = 2; rowIndex <= testCaseRequestList.size() + 1; rowIndex++) {
                TestCaseRequest testCaseRequest = testCaseRequestList.get(rowIndex - 2);
                if (!Utils.isNotNullAndEmpty(testCaseRequest.getName())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestCaseNameEmptyMessage(), rowIndex);
                }
                if (testCasesService.existsByTestCasesName(testCaseRequest.getName())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestCaseNameAlreadyExistsMessage(), rowIndex);
                }
                if (!subModulesService.existsBySubModuleId(testCaseRequest.getSubModuleId())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModuleNotExistsMessage(), rowIndex);
                }
            }
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getTestCaseFileErrorMessage(), errorMessages));
            } else {
                for (TestCaseRequest testCaseRequest : testCaseRequestList) {
                    testCasesService.saveTestCase(testCaseRequest);
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestCaseSuccessMessage()));
            }

        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getTestCaseValidationSaveMessage()));
        }
    }

    @GetMapping(value = EndpointURI.TESTCASES_BY_ID)
    public ResponseEntity<Object> getAllTestCaseBySubModuleId(@PathVariable Long id) {
        if (!subModulesService.existsBySubModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        List<TestCaseResponse> testCaseResponseList = testCasesService.getAllTestCaseBySubModuleId(id);
        if (testCaseResponseList.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getGetTestCaseNotHaveSubModuleIdMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASES, testCasesService.getAllTestCaseBySubModuleId(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetTestCaseBySubModuleIdSuccessMessage()));

    }

    @DeleteMapping(value = EndpointURI.TESTCASE_BY_ID)
    public ResponseEntity<Object> DeleteTestCaseById(@PathVariable Long id) {
        if (!testCasesService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesNotExistCode(), statusCodeBundle.getTestCasesNotExistsMessage()));
        }
        if (testGroupingService.existsByTestCasesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesDependentCode(), statusCodeBundle.getGetValidationTestCaseAssignedMessage()));
        }
        testCasesService.DeleteTestCaseById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteTestCaseSuccessMessage()));
    }
}
