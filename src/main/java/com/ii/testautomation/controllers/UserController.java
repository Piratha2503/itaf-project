package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.service.DesignationService;
import com.ii.testautomation.service.UserService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private DesignationService designationService;

    public ResponseEntity<Object> verifyUser(@PathVariable String token) {
        if (userService.checkExpiry(token))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getTokenExpiredMessage()));
        if (userService.verifyToken(token))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getEmailVerificationFailureMessage()));
        userService.verifyUser(token);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getEmailVerificationSuccessMessage()));

    }

    @PostMapping(value = EndpointURI.USERS)
    public ResponseEntity<Object> saveUser(@RequestBody UserRequest userRequest) {
        if (!companyUserService.existsById(userRequest.getCompanyUserId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getCompanyUserNotExistCode(),statusCodeBundle.getCompanyUserIdNotExistMessage()));
        if (!designationService.existById(userRequest.getDesignationId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
        if (userService.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getUserEmailAlReadyExistMessage()));
        }
        if (userService.existsByContactNo(userRequest.getContactNumber())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getUserContactNoAlReadyExistsMessage()));
        }
        userService.saveUser(userRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveUserSuccessMessage()));
    }
}