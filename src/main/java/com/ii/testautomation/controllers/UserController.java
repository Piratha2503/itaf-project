package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.service.DesignationService;
import com.ii.testautomation.service.UserService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private DesignationService designationService;

    @PutMapping(EndpointURI.USERS)
    public ResponseEntity<Object> updateUser(@RequestBody UserRequest userRequest) {
        if (userRequest.getId() == null)
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode() ,statusCodeBundle.getUserIdCannotBeNullMessage()));
        if (userRequest.getCompanyUserId() == null)
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode() ,statusCodeBundle.getCompanyUserIdNullMessage()));
        if (userRequest.getDesignationId() == null)
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode() ,statusCodeBundle.getDesignationIdNullMessage()));
        if (!userService.existsByUserId(userRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getUserNotExistCode() ,statusCodeBundle.getUserIdNotExistMessage()));
        if (userService.existsByEmailAndIdNot(userRequest.getEmail(),userRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getUserAlreadyExistCode() ,statusCodeBundle.getUserEmailAlReadyExistMessage()));
        if (!companyUserService.existsById(userRequest.getCompanyUserId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getCompanyUserNotExistCode(),statusCodeBundle.getCompanyUserIdNotExistMessage()));
        if (!designationService.existById(userRequest.getDesignationId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
        if (userService.existsByContactNumberAndIdNot(userRequest.getContactNumber(),userRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getUserAlreadyExistCode() ,statusCodeBundle.getUserContactNumberAlreadyExistMessage()));
        userService.updateUser(userRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getUserUpdateSuccessMessage()));
    }

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
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCompanyUserNotExistCode(), statusCodeBundle.getCompanyUserIdNotExistMessage()));
        if (!designationService.existById(userRequest.getDesignationId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
        if (userService.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getUserEmailAlReadyExistMessage()));
        }
        if (userService.existsByContactNo(userRequest.getContactNumber())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getUserContactNoAlReadyExistsMessage()));
        }
        userService.saveUser(userRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveUserSuccessMessage()));
    }
    //    public ResponseEntity<Object> getUserById(@PathVariable Long id){
//        if(!userService.existsByUsersId(id)){
//            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle))
//        }
//    }
}