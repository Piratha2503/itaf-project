package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.MainModulesRequest;
<<<<<<< HEAD
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

=======
import com.ii.testautomation.entities.MainModules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675

public interface MainModulesService
{
    void insertMainModules(MainModulesRequest mainModulesRequest);
    void deleteMainModules(Long id);
    MainModules getByMainModulesId(Long id);
    void updateMainModules(MainModulesRequest mainModulesRequest);
    Page<MainModules> viewAllMainModulesPage(Pageable pageable);
<<<<<<< HEAD
    List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch);
=======
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
    boolean isExistMainModulesId(Long id);
    boolean isExistMainModulesName(String name);
    boolean isExistPrefix(String prefix);
    boolean isExistModulesId(Long id);
    boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId);
    boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId);
}
