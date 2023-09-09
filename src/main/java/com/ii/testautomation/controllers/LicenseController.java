package com.ii.testautomation.controllers;
import com.ii.testautomation.dto.request.LicenseRequest;
import com.ii.testautomation.dto.search.LicensesSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.LicenseService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.RagexMaintainance;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LicenseController {
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @Autowired
    private RagexMaintainance ragexMaintainance;
    @GetMapping(value= EndpointURI.LICENSES)
    public ResponseEntity<Object> getALlPLicense(@RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size,
                                                 @RequestParam(name = "direction") String direction,
                                                 @RequestParam(name = "sortField") String sortField,
                                                 LicensesSearch licensesSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
        return ResponseEntity.ok(new ContentResponse<>(Constants.PROJECTS,licenseService.multiSearchLicensesWithPagination(pageable, pagination, licensesSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetLicenseSuccessMessage()));

    }

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
