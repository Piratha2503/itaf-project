package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.CompanyUserRequest;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Licenses;
import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.service.CompanyUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    @Autowired
    private CompanyUserRepository companyUserRepository;


    @Override
    public boolean existsByLicenseId(Long id) {
        return companyUserRepository.existsByLicensesId(id);
    }

    @Override
    public boolean isExistCompanyUserName(String companyName) {
        return companyUserRepository.existsByCompanyNameIgnoreCase(companyName);
    }

    @Override
    public boolean isExistByCompanyUserEmail(String email) {
        return companyUserRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean isExistByCompanyUserContactNumber(String contactNumber) {
        return companyUserRepository.existsByContactNumber(contactNumber);
    }

    @Override
    public boolean isExistsByFirstNameAndLastName(String firstName, String lastName) {
        return companyUserRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    @Override
    public void saveCompanyUser(CompanyUserRequest companyUserRequest) {
        CompanyUser companyUser=new CompanyUser();
        Licenses licenses=new Licenses();
        licenses.setId(companyUserRequest.getLicenses_id());
        companyUser.setLicenses(licenses);
        BeanUtils.copyProperties(companyUserRequest,companyUser);
        companyUserRepository.save(companyUser);
    }
}

