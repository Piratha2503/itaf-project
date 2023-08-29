package com.ii.testautomation.controllers;
import com.ii.testautomation.dto.request.EmailRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.ExecutionHistoryService;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
public class ExecutionHistoryController {
    @Autowired
    private ExecutionHistoryService executionHistoryService;
    @Autowired
    private TestGroupingService testGroupingService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @GetMapping(EndpointURI.EXECUTION_HISTORY_BY_TEST_GROUPING_ID)
    public ResponseEntity<Object> viewByTestGroupingId(@PathVariable Long id) {

        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        if (!executionHistoryService.existByTestGropingId(id)) {
            return ResponseEntity.ok(
                    new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                            statusCodeBundle.getTestGroupingNotMappedMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.EXECUTION_HISTORY,
                executionHistoryService.viewByTestGroupingId(id), RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getViewExecutionHistoryMessage()));
    }

    @GetMapping(EndpointURI.EXECUTION_HISTORY_ID)
    public ResponseEntity<String> viewReportByExecutionHistoryId(@PathVariable Long id) throws IOException {
        if (id == null) return ResponseEntity.ok(statusCodeBundle.getExecutionHistoryIdNull());
        if (!executionHistoryService.existByExecutionHistoryId(id))
            return ResponseEntity.ok(statusCodeBundle.getExecutionHistoryNotFound());
        return ResponseEntity.ok(executionHistoryService.viewReportByExecutionHistoryId(id));
    }

    @GetMapping(EndpointURI.EXECUTION_HISTORY_BY_DATE)
    public ResponseEntity<String> viewReportWithLastUpdateByExecutionHistoryId(@PathVariable Long id) throws IOException {
        if (id == null) {
            return ResponseEntity.ok(statusCodeBundle.getExecutionHistoryIdNull());
        }
        if (!executionHistoryService.existByExecutionHistoryId(id)) {
            return ResponseEntity.ok(statusCodeBundle.getExecutionHistoryNotFound());
        }
        String response = executionHistoryService.viewReportWithLastUpdateByExecutionHistoryId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(EndpointURI.EXECUTION_HISTORY_DATE_FILTER)
    public ResponseEntity<Object> executionHistoryDateFilter(@PathVariable Long id, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) throws ParseException {

        if (!testGroupingService.existsByTestGroupingId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }

        if (!testGroupingService.existsByTestGroupingId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingNotExistCode(), statusCodeBundle.getTestGroupingNotExistsMessage()));
        if (!executionHistoryService.existByTestGropingId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getTestGroupingNotMappedMessage()));

        Timestamp startingDate;
        Timestamp endingDate;

        if (startDate.isEmpty() || startDate.isBlank()) {
            startingDate = Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1));
            endingDate = Timestamp.valueOf(LocalDateTime.now());
        } else {
            startingDate = new Timestamp(Date.valueOf(startDate).getTime());
            endingDate = new Timestamp(Date.valueOf(endDate).getTime());
        }

        return ResponseEntity.ok(new ContentResponse<>(Constants.EXECUTION_HISTORY, executionHistoryService.executionHistoryDateFilter(id, startingDate, endingDate), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getViewExecutionHistoryMessage()));

    }

    @DeleteMapping(value = EndpointURI.EXECUTION_HISTORY_PROJECT_ID)
    public ResponseEntity<Object> deleteExecutionHistoryById(@PathVariable Long id, @PathVariable Long projectId) {
        if (!executionHistoryService.existByExecutionHistoryId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getExecutionHistoryNotExistsCode(), statusCodeBundle.getExecutionHistoryNotFound()));
        }
        if (!projectService.existByProjectId(projectId)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (!executionHistoryService.deleteExecutionHistory(id, projectId)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getGetFileNotExits()));
        }
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getExecutionHistoryDeleteSuccessMessage()));
    }

    @PostMapping(value = EndpointURI.EXECUTION_HISTORY_EMAIL)
    public ResponseEntity<Object> emailHistoryReports(@RequestBody EmailRequest emailRequest) throws IOException, MessagingException {
        if (emailRequest.getHistoryReportIds() == null || emailRequest.getLastHistoryId() == null || emailRequest.getToEmails() == null)
        return ResponseEntity.ok("Following Requests Cannot be Null or Empty -- > "+"HistoryReportIds,LastHistoryId,ToEmails,Subject");
        if (emailRequest.getHistoryReportIds().isEmpty() || emailRequest.getToEmails().isEmpty())
            return ResponseEntity.ok("Following Requests Cannot be Null or Empty -- > "+"HistoryReportIds,LastHistoryId,ToEmails,Subject");
        executionHistoryService.emailHistoryReports(emailRequest);
        return ResponseEntity.ok("ok");

    }
}