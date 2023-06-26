package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MainModulesService
{
    void insertMainModules(MainModulesRequest mainModulesRequest);
    void deleteMainModules(Long id);
    MainModules getByMainModulesId(Long id);
    void updateMainModules(MainModulesRequest mainModulesRequest);
    Page<MainModules> viewAllMainModulesPage(Pageable pageable);
    List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch);
    boolean isExistMainModulesId(Long id);
    boolean isExistMainModulesName(String name);
    boolean isExistPrefix(String prefix);
    boolean isExistModulesId(Long id);
    boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId);
    boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId);
}
