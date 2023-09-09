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
    public boolean existsById(Long id) {
        return companyUserRepository.existsById(id);
    }
}
