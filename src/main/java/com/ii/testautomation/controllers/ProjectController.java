package com.ii.testautomation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.search.ProjectSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ModulesService;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class ProjectController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ModulesService modulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.PROJECT)
    public ResponseEntity<Object> saveProject(@RequestParam String project,
                                              @RequestParam(value = "jarFile", required = false) MultipartFile jarFile,
                                              @RequestParam(value = "configFile", required = false) MultipartFile configFile) throws JsonProcessingException {
        ProjectRequest projectRequest = objectMapper.readValue(project, ProjectRequest.class);

        if (projectService.existByProjectName(projectRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectAlReadyExistCode(),
                    statusCodeBundle.getProjectNameAlReadyExistMessage()));
        }
        if (projectService.existByProjectCode(projectRequest.getCode())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectAlReadyExistCode(),
                    statusCodeBundle.getProjectCodeAlReadyExistMessage()));
        }
        if (!projectService.checkJarFile(jarFile)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(),
                    statusCodeBundle.getJarfileFailureMessage()));
        }
        if (!projectService.checkPropertiesFile(configFile)) {
            return ResponseEntity.ok(new BaseResponse((RequestStatus.FAILURE.getStatus()), statusCodeBundle.getFileFailureCode(),
                    statusCodeBundle.getConfigFileFailureMessage()));

        }

        projectService.saveProject(projectRequest, jarFile, configFile);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveProjectSuccessMessage()));
    }


    @PutMapping(value = EndpointURI.PROJECT)
    public ResponseEntity<Object> editProject(@RequestParam String project,
                                              @RequestParam(value = "jarFile", required = false) MultipartFile jarFile,
                                              @RequestParam(value = "configFile", required = false) MultipartFile configFile) throws
            JsonProcessingException {
        ProjectRequest projectRequest = objectMapper.readValue(project, ProjectRequest.class);
        if (!projectService.existByProjectId(projectRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (projectService.isUpdateProjectCodeExist(projectRequest.getCode(), projectRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectAlReadyExistCode(),
                    statusCodeBundle.getProjectCodeAlReadyExistMessage()));
        }
        if (projectService.isUpdateProjectNameExist(projectRequest.getName(), projectRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectAlReadyExistCode(),
                    statusCodeBundle.getProjectNameAlReadyExistMessage()));
        }
        if (!projectService.checkJarFile(jarFile)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(),
                    statusCodeBundle.getJarfileFailureMessage()));
        }
        if (!projectService.checkPropertiesFile(configFile)) {
            return ResponseEntity.ok(new BaseResponse((RequestStatus.FAILURE.getStatus()), statusCodeBundle.getFileFailureCode(),
                    statusCodeBundle.getConfigFileFailureMessage()));
        }
        projectService.saveProject(projectRequest, jarFile, configFile);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateProjectSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.PROJECTS)
    public ResponseEntity<Object> getALlProjects(@RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size,
                                                 @RequestParam(name = "direction") String direction,
                                                 @RequestParam(name = "sortField") String sortField,
                                                 ProjectSearch projectSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
        return ResponseEntity.ok(new ContentResponse<>(Constants.PROJECTS, projectService.multiSearchProject(pageable, pagination, projectSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllProjectSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.PROJECT_BY_ID)
    public ResponseEntity<Object> getProjectById(@PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.PROJECT, projectService.getProjectById(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetProjectSuccessMessage()));
    }

    @DeleteMapping(value = EndpointURI.PROJECT_BY_ID)
    public ResponseEntity<Object> deleteProject(@PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        if (modulesService.existsModuleByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectIdDependentCode(),
                    statusCodeBundle.getProjectIdDependentMessage()));
        }

        projectService.deleteProject(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteProjectSuccessMessage()
        ));
    }
}