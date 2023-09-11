package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.CompanyUserRequest;

public interface CompanyUserService {
    void saveCompanyUser(CompanyUserRequest companyUserRequest);
    boolean existsByCompanyUserId(Long id);

    boolean isUpdateCompanyUserNameExists(String name, Long id);
   boolean isUpdateEmailExists(String email,Long id);

boolean isUpdateCompanyUserContactNumberExists(String contactNumber,Long id);
    boolean existsByLicenseId(Long id);
}
