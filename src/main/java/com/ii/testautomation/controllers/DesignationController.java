package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.DesignationService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class DesignationController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private DesignationService designationService;

    @PostMapping(EndpointURI.DESIGNATION)
    public ResponseEntity<Object> saveDesignation(@RequestBody DesignationRequest designationRequest) {
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

    @GetMapping(value=EndpointURI.DESIGNATION_BY_COMPANY_ID)
    public ResponseEntity<Object> getAllDesignationsByCompanyId(@PathVariable Long companyId) {
     List<Designation> designations = designationService.getAllDesignationByCompanyId(companyId);
        if (designations.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getDesignationNotExistsCode(), statusCodeBundle.getDesignationNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.DESIGNATIONS, designations,
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetDesignationSuccessMessage()));
    }


}
