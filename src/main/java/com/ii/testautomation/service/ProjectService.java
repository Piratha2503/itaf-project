package com.ii.testautomation.service;
import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.response.ProjectResponse;
import com.ii.testautomation.dto.search.ProjectSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ProjectService {
    void saveProject(ProjectRequest projectRequest);

    boolean existByProjectName(String projectName);

    boolean existByProjectCode(String projectCode);

    boolean isUpdateProjectNameExist(String projectName, Long projectId);

    boolean isUpdateProjectCodeExist(String projectCode, Long projectId);

    boolean existByProjectId(Long projectId);

    ProjectResponse getProjectById(Long projectId);

    List<ProjectResponse> multiSearchProject(Pageable pageable, PaginatedContentResponse.Pagination pagination, ProjectSearch projectSearch);

    void deleteProject(Long projectId);

    Map<Integer, ProjectRequest> csvToProjectRequest(InputStream inputStream);

    boolean hasExcelFormat(MultipartFile multipartFile);

    Map<Integer, ProjectRequest> excelToProjectRequest(MultipartFile multipartFile);

    void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value);

    boolean isExcelHeaderMatch(MultipartFile multipartFile);

    boolean isCSVHeaderMatch(MultipartFile multipartFile);
}



