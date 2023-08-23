package com.ii.testautomation.controllers;

import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.SchedulingService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SchedulingController {
    @Autowired
    private SchedulingService schedulingService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @DeleteMapping(value = EndpointURI.SCHEDULING_BY_ID)
    public ResponseEntity<Object> deleteSchedulingById(@PathVariable Long id)
    {
        if(!schedulingService.existById(id))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSchedulingNotExistCode(),
                    statusCodeBundle.getSchedulingNotExistMessage()));
        }
        schedulingService.deleteScheduling(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteSchedulingSuccessMessage()));
    }
}
