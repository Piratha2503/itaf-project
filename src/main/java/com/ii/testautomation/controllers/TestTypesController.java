package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.service.TestTypesService;
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
public class TestTypesController {
    @Autowired
    private TestTypesService testTypesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private TestGroupingService testGroupingService;

    @PostMapping(EndpointURI.TEST_TYPE)
    public ResponseEntity<Object> insertTestTypes(@RequestBody TestTypesRequest testTypesRequest) {
        if (testTypesService.isExistsTestTypeByName(testTypesRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(
                    RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getAlreadyExistCode(),
                    statusCodeBundle.getTestTypeNameAlReadyExistMessage()));

        testTypesService.saveTestTypes(testTypesRequest);
        return ResponseEntity.ok(new BaseResponse(
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getInsertTestTypesSuccessMessage()));

    }

    @PutMapping(EndpointURI.TEST_TYPE)
    public ResponseEntity<Object> updateTestTypes(@RequestBody TestTypesRequest testTypesRequest) {
        if (!testTypesService.isExistsTestTypeById(testTypesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(
                    RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeNotExistCode(),
                    statusCodeBundle.getTestTypeIdNotFoundMessage()));
        if (testTypesService.isExistsTestTypesByNameIgnoreCaseAndIdNot(
                testTypesRequest.getName(), testTypesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(
                    RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeAlReadyExistCode(),
                    statusCodeBundle.getTestTypeNameAlReadyExistMessage()));

        testTypesService.saveTestTypes(testTypesRequest);
        return ResponseEntity.ok(new BaseResponse(
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateTestTypeSuccessMessage()));
    }

    @DeleteMapping(EndpointURI.TEST_TYPE_BY_ID)
    public ResponseEntity<Object> deleteTestTypeById(@PathVariable Long id) {
        if (!testTypesService.isExistsTestTypeById(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeNotExistCode(),
                    statusCodeBundle.getTestTypeIdNotFoundMessage()));
        if (testGroupingService.existsByTestTypesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeDependentCode(),
                    statusCodeBundle.getTestTypeDependentMessage()));
        testTypesService.deleteTestTypeById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getDeleteTestTypesSuccessMessage()));
    }

    @GetMapping(EndpointURI.TEST_TYPE_BY_ID)
    public ResponseEntity<Object> getTestTypeById(@PathVariable Long id) {
        if (!testTypesService.isExistsTestTypeById(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeNotExistCode(),
                    statusCodeBundle.getTestTypeIdNotFoundMessage()));

        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTTYPE,
                testTypesService.getTestTypeById(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getViewTestTypeforIdSuccessMessage()));
    }

    @GetMapping(EndpointURI.TEST_TYPES_SEARCH)
    public ResponseEntity<Object> SearchTestTypesWithPagination(@RequestParam(name = "page") int page,
                                                                @RequestParam(name = "size") int size,
                                                                @RequestParam(name = "direction") String direction,
                                                                @RequestParam(name = "sortField") String sortField,
                                                                TestTypesSearch testTypesSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);

        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTTYPES,
                testTypesService.SearchTestTypesWithPagination(pageable, pagination, testTypesSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessageMainModules()));
    }

    @PostMapping(EndpointURI.TEST_TYPE_IMPORT)
    public ResponseEntity<Object> importTestTypes(@RequestParam MultipartFile multipartFile) {

        Map<String, List<Integer>> errorMessages = new HashMap<>();
        List<TestTypesRequest> testTypesRequestList;
        Set<String> testTypeNames = new HashSet<>();

        try {
            if (multipartFile.getOriginalFilename().endsWith(".csv")) {
                if (!testTypesService.isCSVHeaderMatch(multipartFile))
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
                testTypesRequestList = testTypesService.csvProcess(multipartFile.getInputStream());
            } else if (testTypesService.hasExcelFormat(multipartFile)) {
                if (!testTypesService.isExcelHeaderMatch(multipartFile))
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));

                testTypesRequestList = testTypesService.excelProcess(multipartFile);
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }

            for (int rowIndex = 2; rowIndex <= testTypesRequestList.size() + 1; rowIndex++) {
                TestTypesRequest testTypesRequest = testTypesRequestList.get(rowIndex - 2);

                if (!Utils.isNotNullAndEmpty(testTypesRequest.getName())) {
                    testTypesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestTypeNameEmptyMessage(), rowIndex);
                } else if (testTypeNames.contains(testTypesRequest.getName())) {
                    testTypesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestTypeNameDuplicateMessage(), rowIndex);
                } else {
                    testTypeNames.add(testTypesRequest.getName());
                }
                if (!Utils.isNotNullAndEmpty(testTypesRequest.getDescription())) {
                    testTypesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestTypeDescriptionEmptyMessage(), rowIndex);
                }
                if (testTypesService.isExistsTestTypeByName(testTypesRequest.getName())) {
                    testTypesService.addToErrorMessages(errorMessages, statusCodeBundle.getTestTypeNameAlReadyExistMessage(), rowIndex);
                }
            }

            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getTestTypesNotSavedMessage(),
                        errorMessages));
            } else {
                for (TestTypesRequest testTypesRequest : testTypesRequestList) {
                    testTypesService.saveTestTypes(testTypesRequest);
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                        statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getInsertTestTypesSuccessMessage()));
            }
        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getFileFailureMessage()));
        }
    }
}
