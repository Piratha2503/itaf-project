package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.search.ProjectSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
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


@RestController
@CrossOrigin
public class ProjectController {
    @Autowired
    private ProjectService projectService;
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
//    @PostMapping(value = EndpointURI.PROJECT_IMPORT)
//    public ResponseEntity<Object> saveProjectByImportFile(MultipartFile multipartFile)
//    {
//        List<ProjectRequest> projectRequestList=projectService.importProjectFile(multipartFile);
//        for (ProjectRequest projectRequest:projectRequestList
//             ) {
//            if(!Utils.isNotNullAndEmpty(projectRequest.getName()))
//            {
//                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
//                        statusCodeBundle.getProjectFileEmptyCode(),
//                        statusCodeBundle.getProjectNameEmptyMessage()));
//            }
//            if(Utils.isNotNullAndEmpty(projectRequest.getCode()))
//            {
//                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
//                        statusCodeBundle.getProjectFileEmptyCode(),
//                        statusCodeBundle.getProjectCodeEmptyMessage()));
//            }
//            if(Utils.isNotNullAndEmpty(projectRequest.getDescription()))
//            {
//                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
//                        statusCodeBundle.getProjectFileEmptyCode(),
//                        statusCodeBundle.getProjectDescriptionEmptyMessage()));
//            }
//            if (projectService.existByProjectName(projectRequest.getName())) {
//                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
//                        statusCodeBundle.getProjectAlReadyExistCode(),
//                        statusCodeBundle.getProjectNameAlReadyExistMessage()));
//            }
//            if (projectService.existByProjectCode(projectRequest.getCode())) {
//                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
//                        statusCodeBundle.getProjectAlReadyExistCode(),
//                        statusCodeBundle.getProjectCodeAlReadyExistMessage()));
//            }
//        }
//        projectService.saveProjectList(projectRequestList);
//        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
//                statusCodeBundle.getCommonSuccessCode(),
//                statusCodeBundle.getSaveProjectSuccessMessage()));
//
//    }

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
                                                 ProjectSearch projectSearch)
    {
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
        projectService.deleteProject(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getGetProjectDeleteMessage()
        ));
    }


}
