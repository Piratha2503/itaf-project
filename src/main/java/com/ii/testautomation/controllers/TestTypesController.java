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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @PostMapping("/bulkTesttypes")
    public ResponseEntity<Object> fileImport(@RequestParam("file") MultipartFile file) throws IOException
    {
        Map<String,List<Object>> MyList = new HashMap<>();
        List<Object> myErrorlist = new ArrayList<>();

        List<TestTypesRequest> testTypesRequestList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream())))
        {

            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] data = line.split(",");
                TestTypesRequest testTypesRequest = new TestTypesRequest();
                testTypesRequest.setName(data[1]);
                testTypesRequest.setDescription(data[2]);
                testTypesRequestList.add(testTypesRequest);
                myErrorlist.add(testTypesRequest);
                myErrorlist.add("Hello");
                myErrorlist.add(true);
                myErrorlist.add("C");
                myErrorlist.add(500);
                myErrorlist.add(5000L);
                List<String> testList =new ArrayList<>();
                testList.add("My name is Piratha");
                myErrorlist.add(testList);
                MyList.put("My Error",myErrorlist);

            }
        }

        catch (Exception e){}

        return ResponseEntity.ok(MyList);
    }
}
