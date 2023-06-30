package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MainModulesService
{   public void saveMainModules(MainModulesRequest mainModulesRequest);
    public void deleteMainModules(Long id);
    public MainModulesResponse getByMainModulesId(Long id);
    public List<MainModules> getMainModulesByModuleId(Long id);
    public List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch);
    public boolean isExistMainModulesId(Long id);
    public boolean isExistMainModulesName(String name);
    public boolean isExistPrefix(String prefix);
    public boolean isExistModulesId(Long id);
    public boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId);
    public boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId);

    public boolean isExistsSubmodulesByMainModule(Long id);
}

