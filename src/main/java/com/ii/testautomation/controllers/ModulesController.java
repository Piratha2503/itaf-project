package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.ModulesRequest;
import com.ii.testautomation.dto.response.ModulesResponse;
import com.ii.testautomation.dto.search.ModuleSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ModulesController {
    @Autowired
    private ModulesService modulesService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MainModulesService mainModulesService;

    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.MODULE)
    public ResponseEntity<Object> saveModule(@RequestBody ModulesRequest modulesRequest) {
        if (modulesService.isModuleExistsByName(modulesRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAlReadyExistsCode(),
                    statusCodeBundle.getModuleNameAlReadyExistsMessage()));
        }
        if (modulesService.isModuleExistsByPrefix(modulesRequest.getPrefix())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAlReadyExistsCode(),
                    statusCodeBundle.getModulePrefixAlReadyExistsMessage()));
        }
        modulesService.saveModule(modulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveModuleSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.MODULE)
    public ResponseEntity<Object> UpdateModule(@RequestBody ModulesRequest modulesRequest) {
        if (!modulesService.existsByModulesId(modulesRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleNotExistsCode(), statusCodeBundle.getModuleNotExistsMessage()));
        }
        if (modulesService.isUpdateModuleNameExists(modulesRequest.getName(), modulesRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAlReadyExistsCode(), statusCodeBundle.getModuleNameAlReadyExistsMessage()));
        }
        if (modulesService.isUpdateModulePrefixExists(modulesRequest.getPrefix(), modulesRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAlReadyExistsCode(), statusCodeBundle.getModulePrefixAlReadyExistsMessage()));
        }
        if (!projectService.existByProjectId(modulesRequest.getProjectId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }
        modulesService.saveModule(modulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getUpdateModuleSuccessMessage()));
    }

    @DeleteMapping(value = EndpointURI.MODULE_BY_ID)
    public ResponseEntity<Object> deleteModuleById(@PathVariable Long id) {
        if (!modulesService.existsByModulesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleNotExistsCode(), statusCodeBundle.getModuleNotExistsMessage()));
        }
        if (mainModulesService.existsMainModuleByModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(), statusCodeBundle.getGetValidationModuleAssignedMessage()));
        }
        modulesService.deleteModuleById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getDeleteModuleSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.MODULES)
    public ResponseEntity<Object> getAllModules(@RequestParam(name = "page") int page,
                                                @RequestParam(name = "size") int size,
                                                @RequestParam(name = "direction") String direction,
                                                @RequestParam(name = "sortField") String sortField,
                                                @RequestParam(name = "searchTerm") String searchTerm,
                                                ModuleSearch moduleSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.MODULES, modulesService.multiSearchModules(pageable, pagination,searchTerm,moduleSearch),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllModuleSuccessMessage()));

    }

    @GetMapping(value = EndpointURI.MODULE_BY_ID)
    public ResponseEntity<Object> getModuleById(@PathVariable Long id) {
        if (!modulesService.existsByModulesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleNotExistsCode(), statusCodeBundle.getModuleNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.MODULE, modulesService.getModuleById(id),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetModuleByIdSuccessMessage()));
    }


    @GetMapping(value = EndpointURI.MODULES_BY_ID)
    public ResponseEntity<BaseResponse> getAllModulesByProjectId(@PathVariable Long id) {
        if (!projectService.existByProjectId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
        }

        List<ModulesResponse> modulesResponseList = modulesService.getAllModuleByProjectId(id);
        if (modulesResponseList.isEmpty()) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleNotHaveProjectMessage()));
        }

        return ResponseEntity.ok(new ContentResponse<>(Constants.MODULES,
                modulesResponseList,
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetModuleByProjectIdSuccessMessage()));
    }

//    @PostMapping(EndpointURI.MODULE_IMPORT)
//    public ResponseEntity<Object> importFile(@RequestParam("multipartFile") MultipartFile multipartFile) {
//            Map<String, List<Integer>> errorMessages = new HashMap<>();
//            List<ModulesRequest> moduleRequestList;
//
//            try {
//                if (modulesService.hasCsvFormat(multipartFile)) {
//                    moduleRequestList = modulesService .csvToModulesRequest(multipartFile.getInputStream());
//                } else if (modulesService.hasExcelFormat(multipartFile)) {
//                    moduleRequestList = modulesService.excelToModulesRequest(multipartFile.getInputStream());
//                } else {
//                    return ResponseEntity.badRequest().body("Invalid file format");
//                }
//
//                int rowIndex = 2;
//                for (ModulesRequest moduleRequest : moduleRequestList) {
//                    if (!Utils.isNotNullAndEmpty(moduleRequest.getName())) {
//                        modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModuleNameEmptyMessage(), rowIndex);
//                    }
//                    if (!Utils.isNotNullAndEmpty(moduleRequest.getPrefix())) {
//                        modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModulePrefixEmptyMessage(), rowIndex);
//                    }
//                    if (modulesService.isModuleExistsByName(moduleRequest.getName())) {
//                        modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModuleNameAlReadyExistsMessage(), rowIndex);
//                    }
//                    if (modulesService.isModuleExistsByPrefix(moduleRequest.getPrefix())) {
//                        modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModulePrefixAlReadyExistsMessage(), rowIndex);
//                    }
//                    rowIndex++;
//                }
//
//                if (!errorMessages.isEmpty()) {
//                    return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(),
//                            statusCodeBundle.getFailureCode(),
//                            statusCodeBundle.getModuleFileErrorMessage(),
//                            errorMessages));
//                } else {
//                    for (ModulesRequest modulesRequest : moduleRequestList) {
//                        modulesService.saveModule(modulesRequest);
//                    }
//                    return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
//                            statusCodeBundle.getCommonSuccessCode(),
//                            statusCodeBundle.getSaveModuleSuccessMessage()));
//                }
//            } catch (IOException e) {
//                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
//                        statusCodeBundle.getFailureCode(),
//                        statusCodeBundle.getSaveModuleValidationMessage()));
//            }
//        }



}