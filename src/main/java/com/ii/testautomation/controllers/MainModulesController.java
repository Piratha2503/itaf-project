package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.MainModulesRequest;
<<<<<<< HEAD
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


=======
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
@RestController
@CrossOrigin
public class MainModulesController
{

    @Autowired
    private MainModulesService mainModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

<<<<<<< HEAD
    @PostMapping(EndpointURI.MAINMODULE)
    public ResponseEntity<Object> insertMainModules(@RequestBody MainModulesRequest mainModulesRequest)
=======
    @PostMapping("/insertMod")
    public ResponseEntity<Object> insertMain(@RequestBody MainModulesRequest mainModulesRequest)
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
    {

        if (!mainModulesService.isExistModulesId(mainModulesRequest.getModuleid()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getNoid()));
        if (mainModulesService.isExistMainModulesName(mainModulesRequest.getName()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getNameAlreadyExist()));
        if (mainModulesService.isExistPrefix(mainModulesRequest.getPrefix()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getPrefixAlreadyExist()));
        mainModulesService.insertMainModules(mainModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessMessageInsert()));
    }
<<<<<<< HEAD
    @DeleteMapping(EndpointURI.MAINMODULE_BY_ID)
    public ResponseEntity<Object> deleteMainModules(@PathVariable Long id)
=======
    @DeleteMapping("/deleteMod/{id}")
    public ResponseEntity<Object> deleteMain(@PathVariable Long id)
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
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
<<<<<<< HEAD
    @GetMapping(EndpointURI.MAINMODULE_BY_ID)
    public ResponseEntity<Object> getMainModulesById(@PathVariable Long id)
=======
    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id)
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
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
<<<<<<< HEAD
    @PutMapping(EndpointURI.MAINMODULE)
    public ResponseEntity<Object> updateMainModules(@RequestBody MainModulesRequest mainModulesRequest)
=======
    @PutMapping("/update")
    public ResponseEntity<Object> updateMod(@RequestBody MainModulesRequest mainModulesRequest)
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
    {
        if (!mainModulesService.isExistMainModulesId(mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainIdnotFound()));
        if (!mainModulesService.isExistModulesId(mainModulesRequest.getModuleid()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getNoid()));
        if (mainModulesService.isUpdateMainModulesNameExist(mainModulesRequest.getName(), mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getNameAlreadyExist()));
        if (mainModulesService.isUpdateMainModulesPrefixExist(mainModulesRequest.getPrefix(), mainModulesRequest.getId()))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getPrefixAlreadyExist()));

        mainModulesService.updateMainModules(mainModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessUpdateMessage()));


    }

<<<<<<< HEAD
    @GetMapping(EndpointURI.MAINMODULES)
    public ResponseEntity<Object> getAllMainModulesPage(Pageable pageable)
=======
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllPage(Pageable pageable)
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675
    {
        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,
                mainModulesService.viewAllMainModulesPage(pageable),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));

    }

<<<<<<< HEAD
    @GetMapping(EndpointURI.MAINMODULEPAGE)
    public ResponseEntity<Object> SearcgMainModuleswithpage(@RequestParam(name = "page") int page,
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

=======
>>>>>>> 0a0f24cb0bbeb23441effb6462c41af7925d5675




}