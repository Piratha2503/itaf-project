package com.ii.testautomation.service;


import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.response.ProjectResponse;
import com.ii.testautomation.dto.search.ProjectSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    public void saveProject(ProjectRequest projectRequest);
    void saveProjectList(List<ProjectRequest> projectRequestList);
    public boolean existByProjectName(String projectName);

    public boolean existByProjectCode(String projectCode);
    public boolean isUpdateProjectNameExist(String projectName, Long projectId);
    public boolean isUpdateProjectCodeExist(String projectCode, Long projectId);
    public boolean existByProjectId(Long projectId);
    public ProjectResponse getProjectById(Long projectId);
    List<ProjectResponse> multiSearchProject(Pageable pageable, PaginatedContentResponse.Pagination pagination,
                                             ProjectSearch projectSearch);
    public void deleteProject(Long projectId);
    public List<ProjectRequest> importProjectFile(MultipartFile multipartFile);


}
