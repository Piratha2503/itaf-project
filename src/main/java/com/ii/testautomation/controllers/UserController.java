//package com.ii.testautomation.controllers;
//
//import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
//import com.ii.testautomation.dto.request.UserRequest;
//import com.ii.testautomation.enums.RequestStatus;
//import com.ii.testautomation.response.common.BaseResponse;
//import com.ii.testautomation.service.UserService;
//import com.ii.testautomation.utils.EndpointURI;
//import com.ii.testautomation.utils.StatusCodeBundle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@CrossOrigin
//public class UserController {
//    @Autowired
//    UserService userService;
//    @Autowired
//    StatusCodeBundle statusCodeBundle;
//
//    @PostMapping(value = EndpointURI.USERS)
//    public ResponseEntity<Object> saveUser(@RequestBody UserRequest userRequest) {
//        if(userService.existsByStaffIdIgnoreCase(userRequest.getStaffId())){
//            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getUserAlreadyExistCode(),statusCodeBundle.getUserIdExistMessage()));
//         }
//        if (userService.existsByEmail(userRequest.getEmail()) ){
//            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getUserAlreadyExistCode(), statusCodeBundle.getUserEmailAlReadyExistMessage()));
//        }
//        userService.saveUser(userRequest);
//        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveUserSuccessMessage()));
//    }
//}
