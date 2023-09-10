package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.LicenseRequest;
import com.ii.testautomation.dto.response.LicenseResponse;

public interface LicenseService {

    void createLicense(LicenseRequest licenseRequest);

    boolean existsByName(String name);

    boolean existsByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long no_of_projects, Long no_of_users);

    boolean existsById(Long id);
}
