package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.DesignationService;
import com.ii.testautomation.service.UserService;
import com.ii.testautomation.utils.*;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class DesignationController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private UserService userService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(EndpointURI.DESIGNATION)
    public ResponseEntity<Object> saveDesignation(@RequestBody DesignationRequest designationRequest) {
        if (designationRequest.getName().isEmpty() || designationRequest.getName() == null) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getDesignationNullValuesMessage()));
        }
        if (!Utils.checkRagexBeforeAfterWords(designationRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSpacesNotAllowedMessage()));
        }
        if (designationService.existsByName(designationRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationAlreadyExistsCode(), statusCodeBundle.getDesignationAlreadyExistsMessage()));
        }
        designationService.saveDesignation(designationRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDesignationSaveSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.DESIGNATION_BY_COMPANY_ID)
    public ResponseEntity<Object> getAllDesignationsByCompanyId(@PathVariable Long companyId) {
        if (!companyUserService.existsById(companyId)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCompanyUserNotExistCode(), statusCodeBundle.getCompanyUserIdNotExistMessage()));
        }
        List<DesignationResponse> designations = designationService.getAllDesignationByCompanyId(companyId);

        if (designations.isEmpty() && designations.equals(null)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCompanyUserNotExistCode(), statusCodeBundle.getGetCompanyuserIdNotHaveDesignation()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.DESIGNATIONS, designationService.getAllDesignationByCompanyId(companyId), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetDesignationSuccessMessage()));
    }

    @DeleteMapping(EndpointURI.DESIGNATION_BY_ID)
    public ResponseEntity<Object> deleteDesignation(@PathVariable Long id) {

        if (!designationService.existsById(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistMessage()));
        }
        if (userService.existsByDesignationId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationDependentCode(), statusCodeBundle.getDesignationDeleteDependentMessage()));
        }
        designationService.deleteDesignationById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDesignationSuccessfullyDeletedMessage()));
    }

    @PutMapping(EndpointURI.DESIGNATION)
    public ResponseEntity<Object> updateDesignation(@RequestBody DesignationRequest designationRequest) {

        if (designationRequest.getId() == null || designationRequest.getName() == null)
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getDesignationNullValuesMessage()));
        if (!designationService.existById(designationRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
        if (designationService.existsByNameIdNot(designationRequest.getId(), designationRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationAlreadyExistsCode(), statusCodeBundle.getDesignationAlreadyExistsMessage()));
        designationService.saveDesignation(designationRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDesignationUpdateSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.DESIGNATION_BY_ID)
    public ResponseEntity<Object> GetDesignationById(@PathVariable Long id) {
        if (!designationService.existById(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.DESIGNATION, designationService.getDesignationById(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetDesignationByIdSuccessMessage()));
    }
}
