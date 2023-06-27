package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.dto.search.TestTypesSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
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
public class TestTypesController
{
    @Autowired
    private TestTypesService testTypesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.TESTTYPE)
    public ResponseEntity<Object> insertTestTypes(@RequestBody TestTypesRequest testTypesRequest)
    {
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

    @PutMapping(EndpointURI.TESTTYPE)
    public ResponseEntity<Object> updateTestTypes(@RequestBody TestTypesRequest testTypesRequest)
    {
        if (!testTypesService.isExistsTestTypeById(testTypesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(
                    RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeNotExistCode(),
                    statusCodeBundle.getTestTypeIdNotFoundMessage()));
        if (testTypesService.isExistsTestTypesByNameIgnoreCaseAndIdNot(
                testTypesRequest.getName(),testTypesRequest.getId()))
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

    @DeleteMapping(EndpointURI.TESTTYPE_BY_ID)
    public ResponseEntity<Object> deleteTestTypeById(@PathVariable Long id)
    {
        if (!testTypesService.isExistsTestTypeById(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestTypeNotExistCode(),
                    statusCodeBundle.getTestTypeIdNotFoundMessage()));
        testTypesService.deleteTestTypeById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getDeleteTestTypesSuccessMessage()));
    }

    @GetMapping(EndpointURI.TESTTYPE_BY_ID)
    public ResponseEntity<Object> getTestTypeById(@PathVariable Long id)
    {
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

    @GetMapping(EndpointURI.TESTTYPES_SEARCH)
    public ResponseEntity<Object> SearchTestTypesWithPagination(@RequestParam(name = "page") int page,
                                                                @RequestParam(name = "size") int size,
                                                                @RequestParam(name = "direction") String direction,
                                                                @RequestParam(name = "sortField") String sortField,
                                                                TestTypesSearch testTypesSearch)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);

        return ResponseEntity.ok(new ContentResponse<>(Constants.TESTTYPES,
                testTypesService.SearchTestTypesWithPagination(pageable, pagination, testTypesSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));
    }

}
