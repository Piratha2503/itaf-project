package com.ii.testautomation.service;

public interface CompanyUserService {
    boolean existsByLicenseId(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}
