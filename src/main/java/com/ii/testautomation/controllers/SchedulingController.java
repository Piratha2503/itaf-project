package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.SchedulingRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.*;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.SchedulingService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        if (!projectService.existByProjectId(schedulingRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getProjectNotExistCode(), statusCodeBundle.getProjectNotExistsMessage()));
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

     @GetMapping(EndpointURI.SCHEDULING_BY_ID)

    public ResponseEntity<Object>getSchedulingById(@PathVariable Long id){
      if (!schedulingService.existsById(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getSchedulingIdNotExistMessage() , statusCodeBundle.getSchedulingIdNotExistMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.SCHEDULING, schedulingService.getSchedulingById(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetSchedulingSuccessMessage()));

    }

}

        schedulingService.saveTestScheduling(schedulingRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveTestSchedulingSuccessMessage()));
    }

    public ResponseEntity<Object> Execution() {
        schedulingService.autoScheduling();
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getExecutionSuccessMessage()));
    }
}