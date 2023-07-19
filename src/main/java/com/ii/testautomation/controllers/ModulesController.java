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
import java.io.InputStream;
import java.util.*;

@SuppressWarnings("ALL")
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
        if (!projectService.existByProjectId(modulesRequest.getProject_id())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getProjectNotExistCode(),
                    statusCodeBundle.getProjectNotExistsMessage()));
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
        if (!projectService.existByProjectId(modulesRequest.getProject_id())) {
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
                    statusCodeBundle.getModuleDependentCode(), statusCodeBundle.getGetValidationModuleAssignedMessage()));
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
                                                ModuleSearch moduleSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.MODULES, modulesService.multiSearchModules(pageable, pagination, moduleSearch),
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

    @PostMapping(value = EndpointURI.MODULE_IMPORT)
    public ResponseEntity<Object> importModuleFile(@RequestParam MultipartFile multipartFile) {
        Map<String, List<Integer>> errorMessages = new HashMap<>();
        List<ModulesRequest> modulesRequestList = new ArrayList<>();
        Set<String> modulesNames = new HashSet<>();
        Set<String> modulesPrefix = new HashSet<>();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (multipartFile.getOriginalFilename().endsWith(".csv")) {
                if (!modulesService.isCSVHeaderMatch(multipartFile)) {
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                            statusCodeBundle.getHeaderNotExistsMessage()));
                } else {
                    modulesRequestList = modulesService.csvToModulesRequest(inputStream);
                }
            } else if (modulesService.hasExcelFormat(multipartFile)) {
                if (!modulesService.isExcelHeaderMatch(multipartFile)) {
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                            statusCodeBundle.getHeaderNotExistsMessage()));
                } else {
                    modulesRequestList = modulesService.excelToModuleRequest(multipartFile);
                }
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }
            for (int rowIndex = 2; rowIndex <= modulesRequestList.size() + 1; rowIndex++) {

                ModulesRequest modulesRequest = modulesRequestList.get(rowIndex - 2);
                if (!Utils.isNotNullAndEmpty(modulesRequest.getName())) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModuleNameEmptyMessage(), rowIndex);
                } else if (modulesNames.contains(modulesRequest.getName())) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModuleNameDuplicateMessage(), rowIndex);
                } else {
                    modulesNames.add(modulesRequest.getName());
                }

                if (!Utils.isNotNullAndEmpty(modulesRequest.getPrefix())) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModulePrefixEmptyMessage(), rowIndex);
                } else if (modulesPrefix.contains(modulesRequest.getPrefix())) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModulePrefixDuplicateMessage(), rowIndex);
                } else {
                    modulesPrefix.add(modulesRequest.getPrefix());
                }
                if (modulesService.isModuleExistsByName(modulesRequest.getName())) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModuleNameAlReadyExistsMessage(), rowIndex);
                }
                if (modulesService.isModuleExistsByPrefix(modulesRequest.getPrefix())) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModulePrefixAlReadyExistsMessage(), rowIndex);
                }
                if (modulesRequest.getProject_id() == null) {
                    modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getModuleProjectIdEmptyMessage(), rowIndex);
                }else if(!projectService.existByProjectId(modulesRequest.getProject_id())) {
                        modulesService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNotExistsMessage(), rowIndex);
                }
            }
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getModuleFileErrorMessage(), errorMessages));
            } else if(modulesRequestList.isEmpty()) {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFileFailureCode(),statusCodeBundle.getModuleFileEmptyMessage()));
            }else{
                for (ModulesRequest modulesRequest : modulesRequestList) {
                    modulesService.saveModule(modulesRequest);
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getSaveModuleSuccessMessage()));
            }
        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSaveModuleValidationMessage()));
        }
    }
}