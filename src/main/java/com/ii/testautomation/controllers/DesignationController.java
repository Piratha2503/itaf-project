package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.DesignationService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(new ContentResponse<>(Constants.DESIGNATION, designationService.GetDesignationById(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetDesignationByIdSuccessMessage()));
    }

}
