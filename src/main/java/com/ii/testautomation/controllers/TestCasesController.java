package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ProjectService;
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
import java.util.*;

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
    @Autowired
    private ProjectService projectService;

    @PostMapping(value = EndpointURI.TESTCASE)
    public ResponseEntity<Object> saveTestCase(@RequestBody TestCaseRequest testCaseRequest) {
        if (!subModulesService.existsBySubModuleId(testCaseRequest.getSubModuleId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getSubModulesNotExistCode(), statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        if (testCasesService.existsByTestCasesName(testCaseRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestCasesAlreadyExistsCode() , statusCodeBundle.getTestCaseNameAlreadyExistsMessage()));
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
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASES, testCasesService.multiSearchTestCase(pageable, pagination, testCaseSearch), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetAllTestCasesSuccessMessage()));
    }

    @PostMapping(EndpointURI.TESTCASE_IMPORT)
    public ResponseEntity<Object> testCaseImport(@RequestParam MultipartFile multipartFile) {
        Map<String, List<Integer>> errorMessages = new HashMap<>();
        Map<Integer, TestCaseRequest> testCaseRequestList;
        Set<String> testCasesNames = new HashSet<>();
        try {
            if (!testCasesService.isCSVHeaderMatch(multipartFile) && (!testCasesService.isExcelHeaderMatch(multipartFile))) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
            }
            if (Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(".csv")) {
                testCaseRequestList = testCasesService.csvToTestCaseRequest(multipartFile.getInputStream());
            } else if (testCasesService.hasExcelFormat(multipartFile)) {
                testCaseRequestList = testCasesService.excelToTestCaseRequest(multipartFile);
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }
            for (Map.Entry<Integer, TestCaseRequest> entry : testCaseRequestList.entrySet()) {
                if (!Utils.isNotNullAndEmpty(entry.getValue().getName())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestCaseNameEmptyMessage(), entry.getKey());
                } else if (testCasesNames.contains(entry.getValue().getName())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestCaseNameDuplicateMessage(), entry.getKey());
                } else {
                    testCasesNames.add(entry.getValue().getName());
                }
                if (testCasesService.existsByTestCasesName(entry.getValue().getName())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestCaseNameAlreadyExistsMessage(), entry.getKey());
                }
                if (entry.getValue().getSubModuleId() == null) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestcaseSubModuleIdEmptyMessage(), entry.getKey());
                } else if (!subModulesService.existsBySubModuleId(entry.getValue().getSubModuleId())) {
                    testCasesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModuleNotExistsMessage(), entry.getKey());
                }
            }
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getTestCaseFileErrorMessage(), errorMessages));
            } else if (testCaseRequestList.isEmpty()) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getTestcaseFileEmptyMessage()));
            } else {
                for (Map.Entry<Integer, TestCaseRequest> entry : testCaseRequestList.entrySet()) {
                    testCasesService.saveTestCase(entry.getValue());
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

    @GetMapping(value = EndpointURI.TESTCASE_BY_PROJECT_ID)
    public ResponseEntity<Object> getAllTestCasesByProjectId(@PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
        }
        if(!testCasesService.existsTestCaseByProjectId(id)){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),statusCodeBundle.getGetTestCaseNotHaveProjectId()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTCASES, testCasesService.getAllTestcasesByProjectId(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllTestCasesSuccessGivenProjectId()));
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
