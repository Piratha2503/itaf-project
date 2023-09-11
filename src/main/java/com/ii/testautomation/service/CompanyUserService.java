package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.CompanyUserRequest;

public interface CompanyUserService {
    boolean existsByLicenseId(Long id);

    boolean isExistCompanyUserName(String companyName);

    boolean isExistByCompanyUserEmail(String email);

    boolean isExistByCompanyUserContactNumber(String contactNumber);

    boolean isExistsByFirstNameAndLastName(String firstName,String lastName);

    void saveCompanyUser(CompanyUserRequest companyUserRequest);
}
