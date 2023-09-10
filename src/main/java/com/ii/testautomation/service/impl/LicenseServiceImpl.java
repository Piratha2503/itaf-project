package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.LicenseRequest;
import com.ii.testautomation.dto.response.LicenseResponse;
import com.ii.testautomation.entities.Licenses;
import com.ii.testautomation.repositories.LicenseRepository;
import com.ii.testautomation.service.LicenseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;

    @Override
    public void createLicense(LicenseRequest licenseRequest) {
        Licenses licenses = new Licenses();
        BeanUtils.copyProperties(licenseRequest,licenses);
        licenseRepository.save(licenses);
    }

    @Override
    public boolean existsByName(String name) {
        return licenseRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByDurationAndNoOfProjectsAndNoOfUsers(Long duration, Long no_of_projects, Long no_of_users) {
        return licenseRepository.existsByDurationAndNoOfProjectsAndNoOfUsers(duration,no_of_projects,no_of_users);
    }

    @Override
    public boolean existsById(Long id) {

        return licenseRepository.existsById(id);
    }
}
