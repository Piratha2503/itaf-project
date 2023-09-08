package com.ii.testautomation.controllers;

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
