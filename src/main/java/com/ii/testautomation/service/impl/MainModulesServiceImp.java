package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.MainModulesRequest;
<<<<<<< HEAD
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.QMainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
=======
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.service.MainModulesService;
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

=======
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675

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

<<<<<<< HEAD
    @Override
    public List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch)
    {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(mainModuleSearch.getName()))
        {
            booleanBuilder.and(QMainModules.mainModules .name.eq(mainModuleSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(mainModuleSearch.getPrefix()))
        {
            booleanBuilder.and(QMainModules.mainModules.prefix.eq(mainModuleSearch.getPrefix()));
        }

        List<MainModulesResponse> mainModulesResponseList = new ArrayList<>();
        Page<MainModules> mainModulesPage = mainModulesRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(mainModulesPage.getTotalElements());
        pagination.setPageSize(mainModulesPage.getTotalPages());
        for (MainModules mainModules : mainModulesPage)
        {
            MainModulesResponse mainModulesResponse = new MainModulesResponse();
            BeanUtils.copyProperties(mainModules, mainModulesResponse);
            mainModulesResponseList.add(mainModulesResponse);
        }
        return mainModulesResponseList;
    }
=======
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
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

