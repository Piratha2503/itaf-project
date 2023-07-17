package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.SubModulesRequest;
import com.ii.testautomation.dto.search.SubModuleSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.service.SubModulesService;
import com.ii.testautomation.service.TestCasesService;
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

@RestController
@CrossOrigin
public class SubModulesController {
    @Autowired
    private SubModulesService subModulesService;
    @Autowired
    private MainModulesService mainModulesService;
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.SUBMODULE)
    public ResponseEntity<Object> saveSubModules(@RequestBody SubModulesRequest subModulesRequest) {
        if (subModulesService.existsBySubModulesName(subModulesRequest.getName())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModuleNameAlReadyExistMessage()));
        }
        if (subModulesService.existsBySubModulesPrefix(subModulesRequest.getPrefix())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModulePrefixAlReadyExistMessage()));
        }
        if (!mainModulesService.isExistMainModulesId(subModulesRequest.getMain_module_Id())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getMainModulesNotExistCode(),
                    statusCodeBundle.getMainModuleNotExistsMessage()));
        }
        subModulesService.saveSubModules(subModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveSubModuleSuccessMessage()));
    }

    @PostMapping(value = EndpointURI.SUBMODULE_IMPORT)
    public ResponseEntity<Object> importSubModuleFile(@RequestParam MultipartFile multipartFile) {
        Map<String, List<Integer>> errorMessages = new HashMap<>();
        List<SubModulesRequest> subModulesRequestList = new ArrayList<>();
        Set<String> subModuleNames = new HashSet<>();
        Set<String> subModulePrefixes = new HashSet<>();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            if (multipartFile.getOriginalFilename().endsWith(".csv")) {
                if (!subModulesService.isCSVHeaderMatch(multipartFile)) {
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
                } else {
                    subModulesRequestList = subModulesService.csvToSubModuleRequest(multipartFile.getInputStream());
                }
            } else if (subModulesService.hasExcelFormat(multipartFile)) {
                if (!subModulesService.isExcelHeaderMatch(multipartFile)) {
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
                } else {
                    subModulesRequestList = subModulesService.excelToSubModuleRequest(multipartFile);
                }
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }
            for (int rowIndex = 2; rowIndex <= subModulesRequestList.size() + 1; rowIndex++) {
                SubModulesRequest subModulesRequest = subModulesRequestList.get(rowIndex - 2);

                if (!Utils.isNotNullAndEmpty(subModulesRequest.getName())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModuleNameEmptyMessage(), rowIndex);
                } else if (subModuleNames.contains(subModulesRequest.getName())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModuleNameDuplicateMessage(), rowIndex);
                } else {
                    subModuleNames.add(subModulesRequest.getName());
                }
                if (!Utils.isNotNullAndEmpty(subModulesRequest.getPrefix())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModulePrefixEmptyMessage(), rowIndex);
                } else if (subModulePrefixes.contains(subModulesRequest.getPrefix())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModulePrefixDuplicateMessage(), rowIndex);
                } else {
                    subModulePrefixes.add(subModulesRequest.getName());
                }
                if (subModulesRequest.getMain_module_Id() == null) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModuleMainModuleIdEmptyMessage(), rowIndex);
                }
                if (subModulesService.existsBySubModulesPrefix(subModulesRequest.getPrefix())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModulePrefixAlReadyExistMessage(), rowIndex);
                }
                if (subModulesService.existsBySubModulesName(subModulesRequest.getName())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getSubModuleNameAlReadyExistMessage(), rowIndex);
                }
                if (!mainModulesService.isExistMainModulesId(subModulesRequest.getMain_module_Id())) {
                    subModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getMainModuleNotExistsMessage(), rowIndex);
                }
            }
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getSubModuleFileImportValidationMessage(),
                        errorMessages));
            } else {
                for (SubModulesRequest subModulesRequest : subModulesRequestList) {
                    subModulesService.saveSubModules(subModulesRequest);
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                        statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getSaveSubModuleSuccessMessage()));
            }
        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getSaveProjectValidationMessage()));
        }
    }

    @PutMapping(value = EndpointURI.SUBMODULE)
    public ResponseEntity<Object> editSubModules(@RequestBody SubModulesRequest subModulesRequest) {
        if (!subModulesService.existsBySubModuleId(subModulesRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(),
                    statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        if (!mainModulesService.isExistMainModulesId(subModulesRequest.getMain_module_Id())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getMainModulesNotExistCode(),
                    statusCodeBundle.getMainModuleNotExistsMessage()));
        }
        if (subModulesService.isUpdateSubModuleNameExits(subModulesRequest.getName(), subModulesRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModuleNameAlReadyExistMessage()));
        }
        if (subModulesService.isUpdateSubModulePrefixExits(subModulesRequest.getPrefix(), subModulesRequest.getId())) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModulePrefixAlReadyExistMessage()));
        }
        subModulesService.saveSubModules(subModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateSubModuleSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.SUBMODULE_BY_ID)
    public ResponseEntity<Object> getSubModuleById(@PathVariable Long id) {
        if (!subModulesService.existsBySubModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(),
                    statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.SUBMODULE, subModulesService.getSubModuleById(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetSubModulesSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.SUBMODULE_BY_MAIN_MODULE_ID)
    public ResponseEntity<Object> getSubModuleByMainModuleId(@PathVariable Long id) {
        if (!mainModulesService.isExistMainModulesId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getMainModulesNotExistCode(),
                    statusCodeBundle.getMainModuleNotExistsMessage()));
        }
        if (!subModulesService.existsByMainModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getGetSubModuleNotHaveMainModuleId()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.SUBMODULES,
                subModulesService.getAllSubModuleByMainModuleId(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetSubModulesSuccessMessage()));
    }

    @GetMapping(value = EndpointURI.SUBMODULES_SEARCH)
    public ResponseEntity<Object> getALlSubModuleWithMultiSearch(@RequestParam(name = "page") int page,
                                                                 @RequestParam(name = "size") int size,
                                                                 @RequestParam(name = "direction") String direction,
                                                                 @RequestParam(name = "sortField") String sortField,
                                                                 SubModuleSearch subModuleSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);
        return ResponseEntity.ok(new ContentResponse<>(Constants.SUBMODULES, subModulesService.multiSearchSubModule(pageable, pagination, subModuleSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetAllSubModuleSuccessMessage()));
    }

    @DeleteMapping(value = EndpointURI.SUBMODULE_BY_ID)
    public ResponseEntity<Object> deleteSubModuleById(@PathVariable Long id) {
        if (!subModulesService.existsBySubModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(),
                    statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        if (testCasesService.existsBySubModuleId(id)) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesDependentCode(),
                    statusCodeBundle.getSubModulesDependentMessage()));
        }
        subModulesService.deleteSubModuleById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getDeleteSubModuleSuccessMessage()));
    }
}
