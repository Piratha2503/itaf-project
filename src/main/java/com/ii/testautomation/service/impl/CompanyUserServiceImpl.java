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
    public void saveCompanyUser(CompanyUserRequest companyUserRequest) {
        CompanyUser companyUser = new CompanyUser();
        Licenses licenses=new Licenses();
        licenses.setId(companyUserRequest.getLicenses_id());
        companyUser.setLicenses(licenses);
        BeanUtils.copyProperties(companyUserRequest, companyUser);
        companyUserRepository.save(companyUser);
    }

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
        return companyUserRepository.existsByContactNumberIgnoreCaseAndLicensesIdAndIdNot(contactNumber,licensesId,id);

    }

    @Override
    public boolean existsByLicenseId(Long id) {
        return companyUserRepository.existsByLicensesId(id);
    }
}
