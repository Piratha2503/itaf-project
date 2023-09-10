package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.CompanyUserRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.service.LicenseService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.RagexMaintainance;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Access;

@RestController
@CrossOrigin
public class CompanyUserController {
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private RagexMaintainance ragexMaintainance;

    @PutMapping(value = EndpointURI.COMPANY_USERS)
    public ResponseEntity<Object> UpdateCompanyUser(@RequestBody CompanyUserRequest companyUserRequest) {

        if (!companyUserService.existsByCompanyUserId(companyUserRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getCompanyUserNotExistsCode(), statusCodeBundle.getCompanyUserNotExistsMessage()));
        }
        if (!ragexMaintainance.checkSpaceBeforeAfterWords(companyUserRequest.getCompanyName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(), statusCodeBundle.getSpacesNotAllowedMessage()));

        if (companyUserService.isUpdateCompanyUserNameExists(companyUserRequest.getCompanyName(), companyUserRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getCompanyUserAlReadyExistsCode(), statusCodeBundle.getCompanyUseNameAlReadyExistsMessage()));
        }
        if (companyUserService.isUpdateEmailExists(companyUserRequest.getEmail(), companyUserRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getCompanyUserAlReadyExistsCode(), statusCodeBundle.getCompanyUseEmailAlReadyExistsMessage()));
        }
        if (companyUserService.isUpdateCompanyUserContactNumberExists(companyUserRequest.getContactNumber(), companyUserRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getCompanyUserAlReadyExistsCode(), statusCodeBundle.getCompanyUseContactNoAlReadyExistsMessage()));
        }
        if(!licenseService.existByLicenseId(companyUserRequest.getLicenses_id())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getLicenseNotExistCode(),
                    statusCodeBundle .getLicenseNotExistsMessage()));
        }
        companyUserService.saveCompanyUser(companyUserRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getUpdateCompanyUserSuccessMessage()));
    }
}
