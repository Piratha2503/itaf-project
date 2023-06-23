package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.ModulesRequest;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.entities.Project;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.service.ModulesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModulesServiceImplementation implements ModulesService {
    @Autowired
    private ModulesRepository modulesRepository;

    @Override
    public void saveModule(ModulesRequest modulesRequest) {
        Modules modules=new Modules();
        Project project=new Project();
        project.setId(modulesRequest.getProjectId());
        modules.setProject(project);
        BeanUtils.copyProperties(modulesRequest,modules);
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
    public boolean isUpdateModuleNameExists(String name,Long id) {
        return modulesRepository.existsByNameIgnoreCaseAndIdNot(name,id);
    }

    @Override
    public boolean isUpdateModulePrefixExists(String prefix, Long id) {
        return modulesRepository.existsByPrefixIgnoreCaseAndIdNot(prefix, id);
    }

    @Override
    public void deleteModuleById(Long id) {
        modulesRepository.deleteById(id);
    }


}
