package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.search.ProjectSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ModulesService;
import com.ii.testautomation.service.ProjectService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import com.ii.testautomation.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ModulesService modulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.PROJECT)
    public ResponseEntity<Object> saveProject(@RequestBody ProjectRequest projectRequest) {

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
        projectService.saveProject(projectRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveProjectSuccessMessage()));
    }

    @PostMapping(value = EndpointURI.PROJECT_IMPORT)
    public ResponseEntity<Object> importProjectFile(@RequestParam MultipartFile multipartFile) {
        Map<String, List<Integer>> errorMessages = new HashMap<>();
        Map<Integer, ProjectRequest> projectRequestList;
        Set<String> projectNames = new HashSet<>();
        Set<String> projectCodes = new HashSet<>();
        try {
            if (!projectService.isCSVHeaderMatch(multipartFile) && !projectService.isExcelHeaderMatch(multipartFile)) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
            }
            if (Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(".csv")) {
                projectRequestList = projectService.csvToProjectRequest(multipartFile.getInputStream());
            } else if (projectService.hasExcelFormat(multipartFile)) {
                projectRequestList = projectService.excelToProjectRequest(multipartFile);
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }
            for (Map.Entry<Integer, ProjectRequest> entry : projectRequestList.entrySet()) {
                if (!Utils.isNotNullAndEmpty(entry.getValue().getName())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameEmptyMessage(), entry.getKey());
                } else if (projectNames.contains(entry.getValue().getName())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameDuplicateMessage(), entry.getKey());
                } else {
                    projectNames.add(entry.getValue().getName());
                }
                if (!Utils.isNotNullAndEmpty(entry.getValue().getCode())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeEmptyMessage(), entry.getKey());
                } else if (projectCodes.contains(entry.getValue().getCode())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeDuplicateMessage(), entry.getKey());
                } else {
                    projectCodes.add(entry.getValue().getCode());
                }
                if (!Utils.isNotNullAndEmpty(entry.getValue().getDescription())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectDescriptionEmptyMessage(), entry.getKey());
                }
                if (projectService.existByProjectName(entry.getValue().getName())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameAlReadyExistMessage(), entry.getKey());
                }
                if (projectService.existByProjectCode(entry.getValue().getCode())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeAlReadyExistMessage(), entry.getKey());
                }
            }
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getProjectFileImportValidationMessage(),
                        errorMessages));
            } else if (projectRequestList.isEmpty()) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFileFailureCode(), statusCodeBundle.getProjectFileEmptyMessage()));
            } else {
                for (Map.Entry<Integer, ProjectRequest> entry : projectRequestList.entrySet()) {
                    projectService.saveProject(entry.getValue());
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                        statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getSaveProjectSuccessMessage()));
            }
        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSaveProjectValidationMessage()));
        }
    }

    @PutMapping(value = EndpointURI.PROJECT)
    public ResponseEntity<Object> editProject(@RequestBody ProjectRequest projectRequest) {
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

        projectService.saveProject(projectRequest);
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




