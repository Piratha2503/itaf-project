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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> importFile(@RequestParam MultipartFile multipartFile) {
        Map<String, List<Integer>> errorMessages = new HashMap<>();
        List<ProjectRequest> projectRequestList;

        try {
            if (multipartFile.getOriginalFilename().endsWith(".csv")) {
                projectRequestList = projectService.csvToProjectRequest(multipartFile.getInputStream());
            } else if (multipartFile.getOriginalFilename().endsWith(".xlsx")) {
                projectRequestList = projectService.excelToProjectRequest(multipartFile.getInputStream());
            } else {
                return ResponseEntity.badRequest().body("Invalid file format");
            }

            for (int rowIndex = 2; rowIndex <= projectRequestList.size() + 1; rowIndex++) {
                ProjectRequest projectRequest = projectRequestList.get(rowIndex - 2);

                if (!Utils.isNotNullAndEmpty(projectRequest.getName())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameEmptyMessage(), rowIndex);
                }
                if (!Utils.isNotNullAndEmpty(projectRequest.getCode())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeEmptyMessage(), rowIndex);
                }
                if (!Utils.isNotNullAndEmpty(projectRequest.getDescription())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectDescriptionEmptyMessage(), rowIndex);
                }
                if (projectService.existByProjectName(projectRequest.getName())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameAlReadyExistMessage(), rowIndex);
                }
                if (projectService.existByProjectCode(projectRequest.getCode())) {
                    projectService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeAlReadyExistMessage(), rowIndex);
                }
            }


            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getProjectFileImportValidationMessage(),
                        errorMessages));
            } else {
                for (ProjectRequest projectRequest : projectRequestList) {
                    projectService.saveProject(projectRequest);
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                        statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getSaveProjectSuccessMessage()));
            }
        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSaveProjectValidationMessage()));
        } finally {
            // Delete the temporary file after processing
            try {
                File tempFile = File.createTempFile("temp", null);
                multipartFile.transferTo(tempFile);
                tempFile.deleteOnExit(); // Use deleteOnExit() instead of delete()
            } catch (IOException e) {
                // Handle any exceptions related to file deletion
                e.printStackTrace();
            }
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
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
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




