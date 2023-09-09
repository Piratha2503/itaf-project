package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Override
    public boolean existsByCompanyUserId(Long id) {
        return companyUserRepository.existsById(id);
    }

    @Override
    public boolean isUpdateCompanyUserNameExists(String name, Long id) {
        Long licensesId = companyUserRepository.findById(id).get().getLicenses().getId();
        return companyUserRepository.existsByCompanyNameIgnoreCaseAndLicensesIdAndIdNot(name, licensesId, id);
    }

    public boolean isUpdateEmailExists(String email, Long id) {
        Long licensesId = companyUserRepository.findById(id).get().getLicenses().getId();
        return companyUserRepository.existsByEmailIgnoreCaseAndLicensesIdAndIdNot(email, licensesId, id);
    }

    @Override
    public boolean isUpdateCompanyUserContactNumberExists(String contactNumber, Long id) {
        Long licensesId = companyUserRepository.findById(id).get().getLicenses().getId();
        return companyUserRepository.existsByContactNoIgnoreCaseAndLicensesIdAndIdNot(contactNumber, licensesId, id);
    }
}
