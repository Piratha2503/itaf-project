package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.response.ExecutionHistoryResponse;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.ExecutionHistoryService;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.html.HTMLDocument;

import javax.swing.text.html.HTML;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
@CrossOrigin
public class ExecutionHistoryController {
    @Autowired
    private ExecutionHistoryService executionHistoryService;
    @Autowired
    private TestGroupingService testGroupingService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @GetMapping(EndpointURI.EXECUTION_HISTORY_BY_TEST_GROUPING_ID)
    public ResponseEntity<Object> viewByTestGroupingId(@PathVariable Long id){

        //if (!testGroupingService.existsByTestGroupingId(id))
       // return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), "70000","existByExecutionHistoryId not Exist"));
       // if(!executionHistoryService.existByTestGropingId(id))
       // return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),"This Test Group Does not Have Any Execution History"));

        return ResponseEntity.ok(new ContentResponse<>(Constants.EXECUTION_HISTORY, executionHistoryService.viewByTestGroupingId(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), "View By TestGroupingId"));

    }

    @GetMapping(EndpointURI.EXECUTION_HISTORY_ID)
    public ResponseEntity<URL> viewReportByExecutionHistoryId(@PathVariable Long id) throws IOException {
        //if (id == null) return ResponseEntity.ok(statusCodeBundle.getExecutionHistoryIdNull());
       // if (!executionHistoryService.existByExecutionHistoryId(id)) return ResponseEntity.ok(statusCodeBundle.getExecutionHistoryNotFound());
        return ResponseEntity.ok(executionHistoryService.viewReportByExecutionHistoryId(id));
    }
}
