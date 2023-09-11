package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.LicenseRequest;

public interface LicenseService {
    void createLicense(LicenseRequest licenseRequest);

    boolean existsByName(String name);

    boolean existsByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long no_of_projects, Long no_of_users);

    boolean isUpdateByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long no_of_projects, Long no_of_users, Long id);

    boolean isUpdateNameExists(String name, Long id);

    void deleteLicenseById(Long id);

    boolean existsById(Long id);
}
