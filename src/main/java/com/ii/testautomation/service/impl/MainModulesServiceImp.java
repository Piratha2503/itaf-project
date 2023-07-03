package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.entities.QMainModules;
import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.SubModulesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainModulesServiceImp implements MainModulesService {
    @Autowired
    private MainModulesRepository mainModulesRepository;
    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private SubModulesRepository subModulesRepository;

    public void saveMainModules(MainModulesRequest mainModulesRequest) {
        MainModules mainModules = new MainModules();
        Modules modules1 = modulesRepository.findById(mainModulesRequest.getModuleId()).get();
        mainModules.setModules(modules1);
        BeanUtils.copyProperties(mainModulesRequest, mainModules);
        mainModulesRepository.save(mainModules);
    }

    public void deleteMainModules(Long id) {
        mainModulesRepository.deleteById(id);
    }

    public MainModulesResponse getByMainModulesId(Long id) {
        MainModulesResponse mainModulesResponse = new MainModulesResponse();
        MainModules mainModules = mainModulesRepository.findById(id).get();
        mainModulesResponse.setModules(mainModules.getModules());
        BeanUtils.copyProperties(mainModules, mainModulesResponse);
        return mainModulesResponse;
    }

    public List<MainModules> getMainModulesByModuleId(Long id) {
        List<MainModules> mainModulesList = mainModulesRepository.findAllByModulesId(id);

        return mainModulesList;
    }

    @Override
    public List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(mainModuleSearch.getName())) {
            booleanBuilder.and(QMainModules.mainModules.name.eq(mainModuleSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(mainModuleSearch.getPrefix())) {
            booleanBuilder.and(QMainModules.mainModules.prefix.eq(mainModuleSearch.getPrefix()));
        }

        List<MainModulesResponse> mainModulesResponseList = new ArrayList<>();
        Page<MainModules> mainModulesPage = mainModulesRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(mainModulesPage.getTotalElements());
        pagination.setPageSize(mainModulesPage.getTotalPages());
        for (MainModules mainModules : mainModulesPage) {
            MainModulesResponse mainModulesResponse = new MainModulesResponse();
            BeanUtils.copyProperties(mainModules, mainModulesResponse);
            mainModulesResponseList.add(mainModulesResponse);
        }
        return mainModulesResponseList;
    }

    public boolean isExistModulesId(Long id) {
        return modulesRepository.existsById(id);
    }

    public boolean isExistMainModulesName(String name) {
        return mainModulesRepository.existsByName(name);
    }

    public boolean isExistPrefix(String prefix) {
        return mainModulesRepository.existsByPrefix(prefix);
    }

    public boolean isExistMainModulesId(Long id) {
        return mainModulesRepository.existsById(id);
    }

    public boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId) {
        return mainModulesRepository.existsByNameIgnoreCaseAndIdNot(mainModuleName, mainModuleId);
    }

    public boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId) {
        return mainModulesRepository.existsByPrefixIgnoreCaseAndIdNot(mainModuleprefix, mainModuleId);
    }

    public boolean isExistsSubmodulesByMainModule(Long id) {
        return subModulesRepository.existsByMainModuleId(id);
    }

    public boolean existsMainModuleByModuleId(Long id) {
        return mainModulesRepository.existsByModulesId(id);
    }

}

