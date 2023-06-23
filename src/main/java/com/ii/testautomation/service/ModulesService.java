package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.ModulesRequest;

public interface ModulesService {
    public void saveModule(ModulesRequest modulesRequest);

    public boolean isModuleExistsByName(String name);

    public boolean isModuleExistsByPrefix(String prefix);
    public boolean existsByModulesId(Long id);
    public boolean isUpdateModuleNameExists(String name,Long id);
    public boolean isUpdateModulePrefixExists(String prefix,Long id);

    public void deleteModuleById(Long id);

}
