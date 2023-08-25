package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.SchedulingRequest;
import com.ii.testautomation.dto.request.SchedulingRequest;
import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.*;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.SchedulingService;
import com.ii.testautomation.service.TestGroupingService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
public class SchedulingController {
    @Autowired
    private SchedulingService schedulingService;
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private TestScenariosService testScenariosService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TestGroupingService testGroupingService;

    @GetMapping(EndpointURI.SHEDULING_PROJECTID)
    public ResponseEntity<Object> viewByProjectId(@RequestParam(name = "page") int page,
                                                  @RequestParam(name = "size") int size,
                                                  @RequestParam(name = "direction") String direction,
                                                  @RequestParam(name = "sortField") String sortField,
                                                  @PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
        return ResponseEntity.ok(new ContentResponse<>(Constants.SCHEDULES, schedulingService.viewByProjectId(id, pageable, pagination), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getScheduleViewSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.SCHEDULES)
    public ResponseEntity<Object> updateScheduling(@RequestBody SchedulingRequest schedulingRequest) {
        if (schedulingService.isUpdateNameExists(schedulingRequest.getName(), schedulingRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSchedulingNameAlreadyExists()));
        }
        if (!schedulingService.existById(schedulingRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getSchedulingNotExistCode(),
                    statusCodeBundle.getSchedulingNotExistMessage()));
        }
        if (!testGroupingService.existsByTestGroupingId(schedulingRequest.getGroupId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getTestGroupingNotExistCode(),
                    statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        if ( (schedulingRequest.getTestCase().isEmpty()||schedulingRequest.getTestCase()==null)&&
                (schedulingRequest.getTestScenario().isEmpty()||schedulingRequest.getTestScenario().isEmpty())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSchedulingTestCasesAndScenarioEmpty()));
        }
        if(!projectService.existByProjectId(schedulingRequest.getProjectId())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        schedulingService.updateScheduling(schedulingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSchedulingUpdateSuccessMessage()));
    }

    @DeleteMapping(value = EndpointURI.SCHEDULING_BY_ID)
    public ResponseEntity<Object> deleteSchedulingById(@PathVariable Long id) {
        if (!schedulingService.existById(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSchedulingNotExistCode(),
                    statusCodeBundle.getSchedulingNotExistMessage()));
        }
        schedulingService.deleteScheduling(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteSchedulingSuccessMessage()));
    }

    @PostMapping(value = EndpointURI.TEST_SCHEDULING)
    public ResponseEntity<Object> saveScheduling(@RequestBody SchedulingRequest schedulingRequest) {
        if((schedulingRequest.getTestScenario()==null||schedulingRequest.getTestScenario().isEmpty())&& (schedulingRequest.getTestCase()==null||schedulingRequest.getTestCase().isEmpty())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),statusCodeBundle.getSchedulingTestCasesAndScenarioEmpty()));
        }
        if (!projectService.existByProjectId(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (!testGroupingService.existsByTestGroupingId(schedulingRequest.getGroupId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestGroupingNotExistCode() ,statusCodeBundle.getTestGroupingNotExistsMessage()));
        }
        for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestScenario().entrySet()) {
            if (!testScenariosService.existsByTestScenarioId(entry.getValue())) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosNotExistCode(), "testScenarioNotExists"));
            }
        }
        for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestCase().entrySet()) {
            if (!testCasesService.existsByTestCasesId(entry.getValue())) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getTestScenariosNotExistCode(), "testScenarioNotExists"));
            }
        }
        if (!projectService.hasJarPath(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getProjectJarPathNotProvideMessage()));
        }
        if (!projectService.hasConfigPath(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getProjectConfigPathNotProvideMessage()));
        }
        if (!testGroupingService.hasExcelPath(schedulingRequest.getGroupId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getExcelPathNotProvideMessage()));
        }
        if (schedulingService.existsBySchedulingNameByTestGroupingAndProjectId(schedulingRequest.getName(), schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getSchedulingNotExistCode(), statusCodeBundle.getSchedulingNameAlreadyExists()));
        }
        schedulingService.saveTestScheduling(schedulingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestSchedulingSuccessMessage()));
    }

    public ResponseEntity<Object> Execution() throws IOException {
        schedulingService.autoScheduling();
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getExecutionSuccessMessage()));
    }
}