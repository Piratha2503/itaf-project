package com.ii.testautomation.controllers;

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
    SchedulingService schedulingService;
    @Autowired
    StatusCodeBundle statusCodeBundle;


    @GetMapping(EndpointURI.SCHEDULING_BY_ID)

    public ResponseEntity<Object>getSchedulingById(@PathVariable Long id){
      if (!schedulingService.existsById(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getSchedulingIdNotExistMessage() , statusCodeBundle.getSchedulingIdNotExistMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.SCHEDULING, schedulingService.getSchedulingById(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetSchedulingSuccessMessage()));

    }

}
