package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface MainModulesService
{   public void saveMainModules(MainModulesRequest mainModulesRequest);
    public void deleteMainModules(Long id);

    // Get By Functions
    public MainModulesResponse getByMainModulesId(Long id);
    public List<MainModulesResponse> getMainModulesByModuleId(Long id);
    public List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch);
    public List<MainModulesResponse> getByMainModulesName(String name);

    // Check Function
    public boolean isExistMainModulesId(Long id);
    public boolean isExistMainModulesName(String name);
    public boolean isExistPrefix(String prefix);
    public boolean isExistModulesId(Long id);
    public boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId);
    public boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId);
    public boolean isExistsSubmodulesByMainModule(Long id);
    public boolean existsMainModuleByModuleId(Long id);
    //public boolean existsByMainModuleId(Long id);

    // Bulk Import
    public boolean hasExcelFormat(MultipartFile multipartFile);
    public List<MainModulesRequest> csvProcess(InputStream inputStream);
    public List<MainModulesRequest> excelProcess(MultipartFile multipartFile);
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);


}