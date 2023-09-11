package com.ii.testautomation.controllers;

import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.service.UserService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    public ResponseEntity<Object> verifyUser(@PathVariable String token) {
        if (userService.checkExpiry(token))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getTokenExpiredMessage()));
        if (userService.verifyToken(token))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getEmailVerificationFailureMessage()));
        userService.verifyUser(token);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getEmailVerificationSuccessMessage()));

    }

    @DeleteMapping(value = EndpointURI.DELETE_USER)
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        if (!userService.existsByUsersId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getUserNotExistCode(),
                    statusCodeBundle.getUserIdExistMessage()));
        }
        if(!projectService.existsByUsersId(id))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getUsersDeleteDependentCode(),
                    statusCodeBundle.getUsersDeleteDependentMessage()));
        }
        userService.deleteUserById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getUserDeleteSuccessMessage()));
    }
}
