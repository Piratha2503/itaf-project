package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.ModulesRequest;
import com.ii.testautomation.dto.response.ModulesResponse;
import com.ii.testautomation.dto.search.ModuleSearch;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.entities.Project;
import com.ii.testautomation.entities.QModules;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ModulesService;
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
public class ModulesServiceImpl implements ModulesService {
    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void saveModule(ModulesRequest modulesRequest) {
        Modules modules = new Modules();
        Project project = new Project();
        project.setId(modulesRequest.getProjectId());
        modules.setProject(project);
        BeanUtils.copyProperties(modulesRequest, modules);
        modulesRepository.save(modules);
    }

    @Override
    public boolean isModuleExistsByName(String name) {
        return modulesRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean isModuleExistsByPrefix(String prefix) {
        return modulesRepository.existsByPrefixIgnoreCase(prefix);
    }

    @Override
    public boolean existsByModulesId(Long id) {
        return modulesRepository.existsById(id);
    }

    @Override
    public boolean isUpdateModuleNameExists(String name, Long id) {
        return modulesRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public boolean isUpdateModulePrefixExists(String prefix, Long id) {
        return modulesRepository.existsByPrefixIgnoreCaseAndIdNot(prefix, id);
    }

    @Override
    public List<ModulesResponse> multiSearchModules(Pageable pageable, PaginatedContentResponse.Pagination pagination, ModuleSearch moduleSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(moduleSearch.getModuleName())) {
            booleanBuilder.and(QModules.modules.name.eq(moduleSearch.getModuleName()));
        }
        if (Utils.isNotNullAndEmpty(moduleSearch.getModulePrefix())) {
            booleanBuilder.and(QModules.modules.prefix.eq(moduleSearch.getModulePrefix()));
        }
        List<ModulesResponse> modulesResponseList = new ArrayList<>();
        Page<Modules> modulesPage = modulesRepository.findAll(booleanBuilder, pageable);
        pagination.setTotalRecords(modulesPage.getTotalElements());
        pagination.setPageSize(modulesPage.getTotalPages());
        for (Modules modules : modulesPage) {
            ModulesResponse modulesResponse = new ModulesResponse();
            modulesResponse.setProjectId(modules.getProject().getId());
            modulesResponse.setProjectName(modules.getProject().getName());
            BeanUtils.copyProperties(modules, modulesResponse);
            modulesResponseList.add(modulesResponse);

        }

        return modulesResponseList;
    }

    @Override
    public ModulesResponse getModuleById(Long id) {
        ModulesResponse modulesResponse = new ModulesResponse();
        Modules module = modulesRepository.findById(id).get();
        modulesResponse.setProjectId(module.getProject().getId());
        modulesResponse.setProjectName(module.getProject().getName());
        BeanUtils.copyProperties(module, modulesResponse);
        return modulesResponse;
    }

    @Override
    public List<ModulesResponse> getAllModuleByProjectId(Long projectId) {
        List<ModulesResponse> modulesResponseList = new ArrayList<>();
        List<Modules> modulesList = modulesRepository.findAllModulesByProjectId(projectId);
        for (Modules module : modulesList) {
            ModulesResponse modulesResponse = new ModulesResponse();
            modulesResponse.setProjectId(module.getProject().getId());
            modulesResponse.setProjectName(module.getProject().getName());
            BeanUtils.copyProperties(module, modulesResponse);
            modulesResponseList.add(modulesResponse);
        }
        return modulesResponseList;
    }

    @Override
    public void deleteModuleById(Long id) {
        modulesRepository.deleteById(id);
    }

    @Override
    public boolean existsModuleByProjectId(Long projectId) {
        return modulesRepository.existsByProjectId(projectId);
    }
}
