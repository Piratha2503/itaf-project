package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.LicenseRepository;
import com.ii.testautomation.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    LicenseRepository licenseRepository;

    @Override
    public boolean existsById(Long id) {
        return licenseRepository.existsById(id);
    }

    @Override
    public boolean isUpdateNameExists(String name, Long id) {
        return licenseRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }
}
