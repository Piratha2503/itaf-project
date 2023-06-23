package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.ModulesRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.ModulesService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ModulesController {
    @Autowired
    private ModulesService modulesService;

    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(value = EndpointURI.MODULE)
    public ResponseEntity<Object> saveModule(@RequestBody ModulesRequest modulesRequest){
        if(modulesService.isModuleExistsByName(modulesRequest.getName())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAllReadyExistsCode(),
                    statusCodeBundle.getModuleNameAllReadyExistsMessage()));
        }
        if(modulesService.isModuleExistsByPrefix(modulesRequest.getPrefix())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAllReadyExistsCode(),
                    statusCodeBundle.getModulePrefixAllReadyExistsMessage()));
        }
        modulesService.saveModule(modulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveModuleSuccessMessage()));
    }

    @PutMapping(value = EndpointURI.MODULE)
    public ResponseEntity<Object> UpdateModule(@RequestBody ModulesRequest modulesRequest){
        if(!modulesService.existsByModulesId(modulesRequest.getId())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleNotExistsCode(),statusCodeBundle.getModuleNotExistsMessage()));
        }
        if(modulesService.isUpdateModuleNameExists(modulesRequest.getName(), modulesRequest.getId())){
           return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                   statusCodeBundle.getModuleAllReadyExistsCode(),statusCodeBundle.getModuleNameAllReadyExistsMessage()));
        }
        if(modulesService.isUpdateModulePrefixExists(modulesRequest.getPrefix(),modulesRequest.getId())){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getModuleAllReadyExistsCode(),statusCodeBundle.getModulePrefixAllReadyExistsMessage()));
        }
        modulesService.saveModule(modulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getUpdateModuleSuccessMessage()));
    }

    @DeleteMapping(value = EndpointURI.MODULE_BY_ID)
    public ResponseEntity<Object> deleteModuleById(@PathVariable Long id){
        if(!modulesService.existsByModulesId(id)){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),statusCodeBundle.getModuleNotExistsMessage()));
        }
        modulesService.deleteModuleById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getDeleteModuleSuccessMessage()));
    }



}
