package com.ii.testautomation.service;

public interface CompanyUserService {
    boolean existsByCompanyUserId(Long id);

    boolean isUpdateCompanyUserNameExists(String name, Long id);


}
