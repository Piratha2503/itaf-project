package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.ModulesRequest;
import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.response.ModulesResponse;
import com.ii.testautomation.dto.search.ModuleSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ModulesService {
    public void saveModule(ModulesRequest modulesRequest);

    public boolean isModuleExistsByName(String name);

    public boolean isModuleExistsByPrefix(String prefix);

    public boolean existsByModulesId(Long id);

    public boolean isUpdateModuleNameExists(String name, Long id);

    public boolean isUpdateModulePrefixExists(String prefix, Long id);

    public List<ModulesResponse> multiSearchModules(Pageable pageable, PaginatedContentResponse.Pagination pagination, ModuleSearch moduleSearch);

    public ModulesResponse getModuleById(Long id);

    public List<ModulesResponse> getAllModuleByProjectId(Long projectId);

    public void deleteModuleById(Long id);

    public boolean existsModuleByProjectId(Long projectId);

    public boolean hasExcelFormat(MultipartFile multipartFile);

    public Map<Integer,ModulesRequest> csvToModulesRequest(InputStream inputStream);

    public Map<Integer,ModulesRequest> excelToModuleRequest(MultipartFile multipartFile);
    public boolean isExcelHeaderMatch(MultipartFile multipartFile);
    public boolean isCSVHeaderMatch(MultipartFile multipartFile);

    public void addToErrorMessages (Map< String, List < Integer >> errorMessages, String key, int value);
}
