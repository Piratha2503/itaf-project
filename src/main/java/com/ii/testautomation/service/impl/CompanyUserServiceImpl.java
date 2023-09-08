package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.CompanyUserRequest;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Licenses;
import com.ii.testautomation.enums.LoginStatus;
import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.repositories.LicenseRepository;
import com.ii.testautomation.service.CompanyUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    @Autowired
    private CompanyUserRepository companyUserRepository;
    @Autowired
    private LicenseRepository licenseRepository;
    @Override
    public void saveCompanyUser(CompanyUserRequest companyUserRequest) {
        CompanyUser companyUser=new CompanyUser();
        BeanUtils.copyProperties(companyUserRequest,companyUser);
        companyUser.setStatus(LoginStatus.NEW.getStatus());
        Licenses licenses=licenseRepository.findById(companyUserRequest.getLicenses_id()).get();
        companyUser.setLicenses(licenses);
        companyUserRepository.save(companyUser);
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return false;
    }
}
