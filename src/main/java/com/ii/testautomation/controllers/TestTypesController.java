package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.TestTypesRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.TestTypesService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
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

}
