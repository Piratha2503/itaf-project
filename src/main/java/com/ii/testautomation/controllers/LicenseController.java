package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.LicenseRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.LicenseService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.RagexMaintainance;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LicenseController {
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private RagexMaintainance ragexMaintainance;

    @PostMapping(EndpointURI.LICENSE)
    public ResponseEntity<Object> createLicense(@RequestBody LicenseRequest licenseRequest)
    {
        if (!ragexMaintainance.checkSpaceBeforeAfterWords(licenseRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(), statusCodeBundle.getSpacesNotAllowedMessage()));
        if (licenseService.existsByName(licenseRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getLicenseAlreadyExistCode(),statusCodeBundle.getLicenseNameAlreadyExistMessage()));
        if (licenseService.existsByDurationAndNoOfProjectsAndNoOfUsers(licenseRequest.getDuration(), licenseRequest.getNoOfProjects(), licenseRequest.getNoOfUsers()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getLicenseAlreadyExistCode(),statusCodeBundle.getLicensePackageAlreadyExistMessage()));
        licenseService.createLicense(licenseRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getLicenseInsertSuccessMessage()));
    }
}
