package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.SubModulesRequest;
import com.ii.testautomation.dto.response.SubModulesResponse;
import com.ii.testautomation.dto.search.SubModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.QSubModules;
import com.ii.testautomation.entities.SubModules;
import com.ii.testautomation.repositories.SubModulesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.SubModulesService;
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
public class SubModulesServiceImpl implements SubModulesService {
    @Autowired
    private SubModulesRepository subModulesRepository;

    @Override
    public void saveSubModules(SubModulesRequest subModulesRequest) {
        SubModules subModules = new SubModules();
        MainModules mainModules = new MainModules();
        mainModules.setId(subModulesRequest.getMain_module_Id());
        subModules.setMainModule(mainModules);
        BeanUtils.copyProperties(subModulesRequest, subModules);
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
        return subModulesRepository.existsByNameIgnoreCaseAndIdNot(subModuleName, subModuleId);
    }

    @Override
    public boolean isUpdateSubModulePrefixExits(String subModulePrefix, Long subModuleId) {
        return subModulesRepository.existsByPrefixIgnoreCaseAndIdNot(subModulePrefix, subModuleId);
    }

    @Override
    public boolean existsBySubModuleId(Long subModuleId) {
        return subModulesRepository.existsById(subModuleId);
    }

    @Override
    public SubModulesResponse getSubModuleById(Long subModuleId) {
        SubModules subModules = subModulesRepository.findById(subModuleId).get();
        SubModulesResponse subModulesResponse = new SubModulesResponse();
        subModulesResponse.setMainModuleName(subModules.getMainModule().getName());
        subModulesResponse.setMainModulePrefix(subModules.getMainModule().getPrefix());
        BeanUtils.copyProperties(subModules, subModulesResponse);
        return subModulesResponse;
    }

    @Override
    public List<SubModulesResponse> getAllSubModuleByMainModuleId(Long mainModuleId) {
        List<SubModules> subModulesList = subModulesRepository.findAllSubModulesByMainModuleId(mainModuleId);
        List<SubModulesResponse> subModulesResponseList = new ArrayList<>();
        for (SubModules subModules : subModulesList
        ) {
            SubModulesResponse subModulesResponse = new SubModulesResponse();
            subModulesResponse.setMainModulePrefix(subModules.getMainModule().getPrefix());
            subModulesResponse.setMainModuleName(subModules.getMainModule().getName());
            BeanUtils.copyProperties(subModules, subModulesResponse);
            subModulesResponseList.add(subModulesResponse);
        }
        return subModulesResponseList;
    }

    @Override
    public List<SubModulesResponse> multiSearchSubModule(Pageable pageable, PaginatedContentResponse.Pagination pagination, SubModuleSearch subModuleSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(subModuleSearch.getMainModuleName())) {
            booleanBuilder.and(QSubModules.subModules.mainModule.name.eq(subModuleSearch.getMainModuleName()));
        }
        if (Utils.isNotNullAndEmpty(subModuleSearch.getMainModulePrefix())) {
            booleanBuilder.and(QSubModules.subModules.mainModule.prefix.eq(subModuleSearch.getMainModulePrefix()));
        }
        if (Utils.isNotNullAndEmpty(subModuleSearch.getName())) {
            booleanBuilder.and(QSubModules.subModules.name.eq(subModuleSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(subModuleSearch.getPrefix())) {
            booleanBuilder.and(QSubModules.subModules.prefix.eq(subModuleSearch.getPrefix()));
        }

        List<SubModulesResponse> subModulesResponseList = new ArrayList<>();
        Page<SubModules> subModulesPage = subModulesRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(subModulesPage.getTotalElements());
        pagination.setPageSize(subModulesPage.getTotalPages());
        for (SubModules subModules : subModulesPage) {
            SubModulesResponse subModulesResponse = new SubModulesResponse();
            subModulesResponse.setMainModuleName(subModules.getMainModule().getName());
            subModulesResponse.setMainModulePrefix(subModules.getMainModule().getPrefix());
            BeanUtils.copyProperties(subModules, subModulesResponse);
            subModulesResponseList.add(subModulesResponse);
        }
        return subModulesResponseList;
    }

    @Override
    public void deleteSubModuleById(Long subModuleId) {
        subModulesRepository.deleteById(subModuleId);
    }

    @Override
    public boolean existsByMainModuleId(Long mainModuleId) {
        return subModulesRepository.existsByMainModuleId(mainModuleId);
    }
}
