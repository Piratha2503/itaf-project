package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.SubModulesRequest;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.SubModules;
import com.ii.testautomation.repositories.SubModulesRepository;
import com.ii.testautomation.service.SubModulesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubModulesServiceImpl implements SubModulesService {
    @Autowired
    private SubModulesRepository subModulesRepository;
    @Override
    public void saveSubModules(SubModulesRequest subModulesRequest) {
        SubModules subModules=new SubModules();
        MainModules mainModules=new MainModules();
        mainModules.setId(subModulesRequest.getMain_module_Id());
        subModules.setMainModule(mainModules);
        BeanUtils.copyProperties(subModulesRequest,subModules);
        subModulesRepository.save(subModules);
    }

    @Override
    public boolean existsBySubModulesName(String subModuleName) {
        return subModulesRepository.existsByNameIgnoreCase(subModuleName);
    }

    @Override
    public boolean existsBySubModulesPrefix(String subModulePrefix) {
        return subModulesRepository.existsByPrefixIgnoreCase(subModulePrefix);
    }

    @Override
    public boolean isUpdateSubModuleNameExits(String subModuleName, Long subModuleId) {
        return subModulesRepository.existsByNameIgnoreCaseAndIdNot(subModuleName,subModuleId);
    }

    @Override
    public boolean isUpdateSubModulePrefixExits(String subModulePrefix, Long subModuleId) {
        return subModulesRepository.existsByPrefixIgnoreCaseAndIdNot(subModulePrefix,subModuleId);
    }

    @Override
    public boolean existsBySubModuleId(Long subModuleId) {
        return subModulesRepository.existsById(subModuleId);
    }
}
