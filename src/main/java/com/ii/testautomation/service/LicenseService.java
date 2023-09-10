package com.ii.testautomation.service;

import com.ii.testautomation.dto.response.LicenseResponse;
import com.ii.testautomation.dto.search.LicensesSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


import com.ii.testautomation.dto.request.LicenseRequest;

public interface LicenseService {
    List<LicenseResponse> multiSearchLicensesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, LicensesSearch licensesSearch);

    void createLicense(LicenseRequest licenseRequest);

    boolean existsByName(String name);

    boolean existsByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long no_of_projects, Long no_of_users);


    boolean isUpdateByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long no_of_projects, Long no_of_users,Long id);

    boolean existsById(Long id);

    boolean isUpdateNameExists(String name, Long id);

    void deleteLicenseById(Long id);


}
