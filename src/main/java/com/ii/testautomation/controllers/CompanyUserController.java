package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.CompanyUserRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CompanyUserController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;

    @PostMapping(EndpointURI.COMPANY_USERS)
    public ResponseEntity<Object>saveCompanyUser(@RequestBody CompanyUserRequest companyUserRequest) {
        if (!companyUserService.existsByLicenseId(companyUserRequest.getLicenses_id())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getLicenseIdNotFoundMessage()));
        }
        if (companyUserService.isExistCompanyUserName(companyUserRequest.getCompanyName())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCompanyUserAlreadyExistCode(), statusCodeBundle.getCompanyUserNameAlreadyExistMessage()));
        }
        if (companyUserService.isExistByCompanyUserEmail(companyUserRequest.getEmail())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getCompanyUserEmailAlreadyExistMessage()));
        }
        if (companyUserService.isExistByCompanyUserContactNumber(companyUserRequest.getContactNumber())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getCompanyUserContactNumberAlreadyExistMessage()));
        }
        if (companyUserService.isExistsByFirstNameAndLastName(companyUserRequest.getFirstName(),companyUserRequest.getLastName())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),statusCodeBundle.getCompanyUserFirstNameAndLastNameAlreadyExistMessage()));
        }
        companyUserService.saveCompanyUser(companyUserRequest);
        return ResponseEntity.ok((new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getCompanyUserSuccessfullyInsertedMessage() )));
    }

}
