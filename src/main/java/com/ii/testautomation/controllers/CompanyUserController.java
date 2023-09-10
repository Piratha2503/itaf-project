package com.ii.testautomation.controllers;

import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CompanyUserController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;

    @DeleteMapping(EndpointURI.COMPANY_USER_BY_ID)
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        if (!companyUserService.existsById(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getCompanyUserIdNotExistMessage()));
        companyUserService.deleteById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getCompanyUserDeleteSuccessMessage()));
    }
}

