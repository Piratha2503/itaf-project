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

public interface MainModulesService {
    void saveMainModules(MainModulesRequest mainModulesRequest);

    void deleteMainModules(Long id);

    MainModulesResponse getByMainModulesId(Long id);

    List<MainModulesResponse> getMainModulesByModuleId(Long id);

    List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch);

    boolean isExistMainModulesId(Long id);

    boolean isExistMainModulesName(String name);

    boolean isExistPrefix(String prefix);

    boolean isExistModulesId(Long id);

    boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId);

    boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId);

    boolean existsMainModuleByModuleId(Long id);

    boolean hasExcelFormat(MultipartFile multipartFile);

    List<MainModulesRequest> csvProcess(InputStream inputStream);

    List<MainModulesRequest> excelProcess(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

    boolean isExcelHeaderMatch(MultipartFile multipartFile);

    boolean isCSVHeaderMatch(MultipartFile multipartFile);

    List<MainModulesResponse> getMainModulesByProjectId(Long id);
}