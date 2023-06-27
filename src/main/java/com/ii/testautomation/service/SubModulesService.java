package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SubModulesRequest;
import com.ii.testautomation.dto.response.SubModulesResponse;
import com.ii.testautomation.dto.search.SubModuleSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubModulesService {
    public void saveSubModules(SubModulesRequest subModulesRequest);
    public boolean existsBySubModulesName(String subModuleName);
    public boolean existsBySubModulesPrefix(String subModulePrefix);
    public boolean isUpdateSubModuleNameExits(String subModuleName,Long subModuleId);
    public boolean isUpdateSubModulePrefixExits(String subModulePrefix,Long subModuleId);
    public boolean existsBySubModuleId(Long subModuleId);
    public SubModulesResponse getSubModuleById(Long subModuleId);
    public List<SubModulesResponse> getAllSubModuleByMainModuleId(Long id);
    public List<SubModulesResponse> multiSearchSubModule(Pageable pageable, PaginatedContentResponse.Pagination pagination, SubModuleSearch subModuleSearch);
    public void deleteSubModuleById(Long subModuleId);
}
