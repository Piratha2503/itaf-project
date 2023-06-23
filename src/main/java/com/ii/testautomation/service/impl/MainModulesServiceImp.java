package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.service.MainModulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainModulesServiceImp implements MainModulesService
{
    String msg = "ok";
    @Autowired
    private MainModulesRepository mainModulesRepository;
    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private ProjectRepository projectRepository;

    public void insertMain(MainModulesRequest mainModulesRequest)
    {
        Modules modules1 = modulesRepository.findById(mainModulesRequest.getModid()).get();
        MainModules mainModules = new MainModules();
        mainModules.setModules(modules1);
        mainModules.setName(mainModulesRequest.getName());
        mainModules.setPrefix(mainModulesRequest.getPrefix());
        mainModulesRepository.save(mainModules);
    }

    public void deleteMain(Long id)
    {
       mainModulesRepository.deleteById(id);
    }

    public void updateMod(MainModulesRequest mainModulesRequest)
    {

        MainModules mainModules = mainModulesRepository.findById(mainModulesRequest.getModid()).get();
        Modules modules1 = mainModules.getModules();
        mainModules.setModules(modules1);
        mainModules.setName(mainModulesRequest.getName());
        mainModules.setPrefix(mainModulesRequest.getPrefix());
        mainModulesRepository.save(mainModules);
    }

    public MainModules getById(Long id)
    {
        MainModules mainModules = mainModulesRepository.findById(id).get();
        return mainModules;
    }

    public Page<MainModules> ViewAll(Pageable pageable)
    {
        Page<MainModules> modulesPage = mainModulesRepository.findAll(pageable);
        return modulesPage;

    }

    public boolean isExistModId(Long id)
    {
        return modulesRepository.existsById(id);
    }

    public boolean isExistMainName(String name)
    {
        return mainModulesRepository.existsByName(name);
    }

    public boolean isExistPrefix(String prefix)
    {
        return mainModulesRepository.existsByPrefix(prefix);
    }

    public boolean isExistMainModId(Long id)
    {
        return mainModulesRepository.existsById(id);
    }


}


