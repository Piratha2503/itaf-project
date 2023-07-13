package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.FileResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class MainModulesController {
    @Autowired
    private MainModulesService mainModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.MAIN_MODULE)
    public ResponseEntity<Object> insertMainModules(@RequestBody MainModulesRequest mainModulesRequest) {

        if (!mainModulesService.isExistModulesId(mainModulesRequest.getModuleId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleIdNotFound()));
        if (mainModulesService.isExistMainModulesName(mainModulesRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getAlreadyExistCode(),
                    statusCodeBundle.getNameAlreadyExist()));
        if (mainModulesService.isExistPrefix(mainModulesRequest.getPrefix()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getAlreadyExistCode(),
                    statusCodeBundle.getPrefixAlreadyExist()));
        mainModulesService.saveMainModules(mainModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessMessageInsertMainModules()));
    }

    @DeleteMapping(EndpointURI.MAIN_MODULE_BY_ID)
    public ResponseEntity<Object> deleteMainModules(@PathVariable Long id) {
        if (!mainModulesService.isExistMainModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainModulesIdNotFound()));

        if (mainModulesService.isExistsSubmodulesByMainModule(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getIdAssignedWithAnotherTable()));

        mainModulesService.deleteMainModules(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessMessageDeleteMainModules()));
    }

    @PutMapping(EndpointURI.MAIN_MODULE)
    public ResponseEntity<Object> updateMainModules(@RequestBody MainModulesRequest mainModulesRequest) {
        if (!mainModulesService.isExistMainModulesId(mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainModulesIdNotFound()));
        if (!mainModulesService.isExistModulesId(mainModulesRequest.getModuleId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleIdNotFound()));
        if (mainModulesService.isUpdateMainModulesNameExist(mainModulesRequest.getName(), mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getAlreadyExistCode(),
                    statusCodeBundle.getNameAlreadyExist()));
        if (mainModulesService.isUpdateMainModulesPrefixExist(mainModulesRequest.getPrefix(), mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getAlreadyExistCode(),
                    statusCodeBundle.getPrefixAlreadyExist()));

        mainModulesService.saveMainModules(mainModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessUpdateMessageMainModules()));
    }

    @GetMapping(EndpointURI.MAIN_MODULE_BY_ID)
    public ResponseEntity<Object> getMainModulesByMainModuleId(@PathVariable Long id) {
        if (!mainModulesService.isExistMainModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainModulesIdNotFound()));

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,
                mainModulesService.getByMainModulesId(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessageMainModules()));
    }

    @GetMapping(EndpointURI.MAIN_MODULE_BY_MODULE_ID)
    public ResponseEntity<Object> getMainModulesByModuleId(@PathVariable Long id) {
        if (!mainModulesService.isExistModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleIdNotFound()));
        if (mainModulesService.getMainModulesByModuleId(id).isEmpty())
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleIdNotAssigned()));

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,
                mainModulesService.getMainModulesByModuleId(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessageMainModules()));
    }

    @GetMapping(EndpointURI.MAIN_MODULE_PAGE)
    public ResponseEntity<Object> SearchMainModulesWithPage(@RequestParam(name = "page") int page,
                                                            @RequestParam(name = "size") int size,
                                                            @RequestParam(name = "direction") String direction,
                                                            @RequestParam(name = "sortField") String sortField,
                                                            MainModuleSearch mainModuleSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES, mainModulesService.SearchMainModulesWithPagination(pageable, pagination, mainModuleSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessageMainModules()));
    }

    @PostMapping(EndpointURI.MAIN_MODULE_IMPORT)
    public ResponseEntity<Object> importMainModules(@RequestParam MultipartFile multipartFile) {

        Map<String, List<Integer>> errorMessages = new HashMap<>();
        List<MainModulesRequest> mainModulesRequestList;

        try {
            if (multipartFile.getOriginalFilename().endsWith(".csv")) {
                if (mainModulesService.isCSVHeaderMatch(multipartFile))
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
                mainModulesRequestList = mainModulesService.csvProcess(multipartFile.getInputStream());
            } else if (mainModulesService.hasExcelFormat(multipartFile)) {
                if (mainModulesService.isExcelHeaderMatch(multipartFile))
                    return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getHeaderNotExistsMessage()));
                mainModulesRequestList = mainModulesService.excelProcess(multipartFile);
            } else {
                return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getFileFailureCode(), statusCodeBundle.getFileFailureMessage()));
            }

            for (int rowIndex = 2; rowIndex <= mainModulesRequestList.size() + 1; rowIndex++) {
                MainModulesRequest mainModulesRequest = mainModulesRequestList.get(rowIndex - 2);

                if (mainModulesRequestList.contains(mainModulesRequest.getName()))

                if (!Utils.isNotNullAndEmpty(mainModulesRequest.getName())) {
                    mainModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameEmptyMessage(), rowIndex);
                }
                if (!Utils.isNotNullAndEmpty(mainModulesRequest.getModuleId().toString())) {
                    mainModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeEmptyMessage(), rowIndex);
                }
                if (!Utils.isNotNullAndEmpty(mainModulesRequest.getPrefix())) {
                    mainModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectDescriptionEmptyMessage(), rowIndex);
                }
                if (mainModulesService.isExistMainModulesName(mainModulesRequest.getName())) {
                    mainModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectNameAlReadyExistMessage(), rowIndex);
                }
                if (mainModulesService.isExistPrefix(mainModulesRequest.getPrefix())) {
                    mainModulesService.addToErrorMessages(errorMessages, statusCodeBundle.getProjectCodeAlReadyExistMessage(), rowIndex);
                }
            }
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.ok(new FileResponse(RequestStatus.FAILURE.getStatus(),
                        statusCodeBundle.getFailureCode(),
                        statusCodeBundle.getMainModulesNotSavedMessage(),
                        errorMessages));
            } else {
                for (MainModulesRequest mainModulesRequest : mainModulesRequestList) {
                    mainModulesService.saveMainModules(mainModulesRequest);
                }
                return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                        statusCodeBundle.getCommonSuccessCode(),
                        statusCodeBundle.getSuccessMessageInsertMainModules()));
            }
        } catch (IOException e) {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getFileFailureMessage()));
        }

    }
}
