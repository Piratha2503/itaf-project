package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.service.MainModulesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MainModulesServiceImp implements MainModulesService
{
    @Autowired
    private MainModulesRepository mainModulesRepository;
    @Autowired
    private ModulesRepository modulesRepository;


    public void insertMainModules(MainModulesRequest mainModulesRequest)
    {
        Modules modules1 = modulesRepository.findById(mainModulesRequest.getModuleid()).get();
        MainModules mainModules = new MainModules();
        mainModules.setModules(modules1);
        mainModules.setName(mainModulesRequest.getName());
        mainModules.setPrefix(mainModulesRequest.getPrefix());
        mainModulesRepository.save(mainModules);
    }

    public void deleteMainModules(Long id)
    {
        mainModulesRepository.deleteById(id);
    }

    public void updateMainModules(MainModulesRequest mainModulesRequest)
    {
        MainModules mainModules = mainModulesRepository.findById(mainModulesRequest.getId()).get();
        Modules modules1 = modulesRepository.findById(mainModulesRequest.getModuleid()).get();
        mainModules.setModules(modules1);
        BeanUtils.copyProperties(mainModulesRequest,mainModules);
        mainModulesRepository.save(mainModules);
    }

    public MainModules getByMainModulesId(Long id)
    {
        MainModules mainModules = mainModulesRepository.findById(id).get();
        return mainModules;
    }

    public Page<MainModules> viewAllMainModulesPage(Pageable pageable)
    {
        Page<MainModules> modulesPage = mainModulesRepository.findAll(pageable);
        return modulesPage;

    }

    public boolean isExistModulesId(Long id)
    {
        return modulesRepository.existsById(id);
    }

    public boolean isExistMainModulesName(String name)
    {
        return mainModulesRepository.existsByName(name);
    }

    public boolean isExistPrefix(String prefix)
    {
        return mainModulesRepository.existsByPrefix(prefix);
    }

    public boolean isExistMainModulesId(Long id)
    {
        return mainModulesRepository.existsById(id);
    }

    public boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId) {
        return mainModulesRepository.existsByNameIgnoreCaseAndIdNot(mainModuleName, mainModuleId);
    }

    public boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId) {
        return mainModulesRepository.existsByPrefixIgnoreCaseAndIdNot(mainModuleprefix,mainModuleId);
    }

}

