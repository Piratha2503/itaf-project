package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class MainModulesController
{

    @Autowired
    private MainModulesService mainModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.MAINMODULE)
    public ResponseEntity<Object> insertMainModules(@RequestBody MainModulesRequest mainModulesRequest)
    {

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
                statusCodeBundle.getSuccessMessageInsert()));
    }

    @DeleteMapping(EndpointURI.MAINMODULE_BY_ID)
    public ResponseEntity<Object> deleteMainModules(@PathVariable Long id)
    {
        if (!mainModulesService.isExistMainModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainIdnotFound()));
        mainModulesService.deleteMainModules(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessMessageDelete()));
    }

    @GetMapping(EndpointURI.MAINMODULE_BY_ID)
    public ResponseEntity<Object> getMainModulesById(@PathVariable Long id)
    {
        if (!mainModulesService.isExistMainModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainIdnotFound()));

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,
                mainModulesService.getByMainModulesId(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));
    }

    @GetMapping(EndpointURI.MAINMODULE_BY_MODULEID)
    public ResponseEntity<Object> getMainModulesByModuleId(@PathVariable Long id)
    {
        if (!mainModulesService.isExistModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleIdNotFound()));
        if (mainModulesService.getByModuleId(id).isEmpty())
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getModuleIdNotAssignedmsg()));

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,
                mainModulesService.getByModuleId(id),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));
    }

    @PutMapping(EndpointURI.MAINMODULE)
    public ResponseEntity<Object> updateMainModules(@RequestBody MainModulesRequest mainModulesRequest)
    {
        if (!mainModulesService.isExistMainModulesId(mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainIdnotFound()));
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
                statusCodeBundle.getSuccessUpdateMessage()));


    }

    @GetMapping(EndpointURI.MAINMODULEPAGE)
    public ResponseEntity<Object> SearchMainModuleswithpage(@RequestParam(name = "page") int page,
                                         @RequestParam(name = "size") int size,
                                         @RequestParam(name = "direction") String direction,
                                         @RequestParam(name = "sortField") String sortField,
                                         MainModuleSearch mainModuleSearch)
    {
           Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
           PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES, mainModulesService.SearchMainModulesWithPagination(pageable, pagination, mainModuleSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));
    }





}