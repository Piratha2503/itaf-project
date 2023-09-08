package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.CompanyUserRequest;

public interface CompanyUserService {
    void saveCompanyUser(CompanyUserRequest companyUserRequest);
    boolean existsByCompanyName(String companyName);

    boolean existsByEmail(String email);

    boolean existsByFirstNameAndLastName(String firstName,String lastName);

}
