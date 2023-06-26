package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.MainModulesRequest;
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
@RestController
@CrossOrigin
public class MainModulesController
{

    @Autowired
    private MainModulesService mainModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping("/insertMod")
    public ResponseEntity<Object> insertMain(@RequestBody MainModulesRequest mainModulesRequest)
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
    @DeleteMapping("/deleteMod/{id}")
    public ResponseEntity<Object> deleteMain(@PathVariable Long id)
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
    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id)
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
    @PutMapping("/update")
    public ResponseEntity<Object> updateMod(@RequestBody MainModulesRequest mainModulesRequest)
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

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllPage(Pageable pageable)
    {
        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,
                mainModulesService.viewAllMainModulesPage(pageable),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));

    }





}