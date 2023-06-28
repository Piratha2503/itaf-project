package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.service.MainModulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainModulesServiceImpl implements MainModulesService {
    @Autowired
    private MainModulesRepository mainModuleRepository;

    @Override
    public boolean existsByMainModuleId(Long mainModuleId) {
        return mainModuleRepository.existsById(mainModuleId);
    }

    @Override
    public boolean existsMainModuleByModuleId(Long moduleId) {
        return mainModuleRepository.existsByModuleId(moduleId);
    }


}
