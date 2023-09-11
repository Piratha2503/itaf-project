package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.service.DesignationService;
import com.ii.testautomation.service.UserService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class DesignationController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private DesignationService designationService;

    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @PostMapping(EndpointURI.DESIGNATION)
    public ResponseEntity<Object> saveDesignation(@RequestBody DesignationRequest designationRequest) {
        if (designationService.existsByName(designationRequest.getName())) {
            public ResponseEntity<Object> saveDesignation (@RequestBody DesignationRequest designationRequest){
                if (designationService.existsByName(designationRequest.getName())) {
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                            statusCodeBundle.getDesignationAlreadyExistsCode(),
                            statusCodeBundle.getDesignationAlreadyExistsMessage()));
                }
                designationService.saveDesignation(designationRequest);
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                        statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getDesignationSaveSuccessMessage()));
            }

            @GetMapping(value = EndpointURI.DESIGNATION_BY_COMPANY_ID)
            public ResponseEntity<Object> getAllDesignationsByCompanyId (@PathVariable Long companyId){
                if (!companyUserService.existsByCompanyId(companyId)) {
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCompanyUserNotExistsCode(), statusCodeBundle.getCompanyUserNotExistsMessage()));
                }
                return ResponseEntity.ok(new ContentResponse<>(Constants.DESIGNATIONS, designationService.getAllDesignationByCompanyId(companyId),
                        RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetDesignationSuccessMessage()));
            }


            @PutMapping(EndpointURI.DESIGNATION)
            public ResponseEntity<Object> updateDesignation (@RequestBody DesignationRequest designationRequest){

                if (!designationService.existById(designationRequest.getId()))
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
                if (designationService.existsByNameIdNot(designationRequest.getId(), designationRequest.getName()))
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationAlreadyExistsCode(), statusCodeBundle.getDesignationAlreadyExistsMessage()));
                designationService.saveDesignation(designationRequest);
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDesignationUpdateSuccessMessage()));

            }

        }