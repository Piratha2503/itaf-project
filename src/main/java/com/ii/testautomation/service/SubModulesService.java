package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SubModulesRequest;

public interface SubModulesService {

    public void saveSubModules(SubModulesRequest subModulesRequest);

    public boolean existsBySubModulesName(String subModuleName);
    public boolean existsBySubModulesPrefix(String subModulePrefix);

    public boolean isUpdateSubModuleNameExits(String subModuleName,Long subModuleId);
    public boolean isUpdateSubModulePrefixExits(String subModulePrefix,Long subModuleId);
    public boolean existsBySubModuleId(Long subModuleId);
}
