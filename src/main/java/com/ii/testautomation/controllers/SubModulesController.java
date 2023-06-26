package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.SubModulesRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.service.SubModulesService;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SubModulesController {
    @Autowired
    private SubModulesService subModulesService;
    @Autowired
    private MainModulesService mainModulesService;

    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.SUBMODULE)
    public ResponseEntity<Object> saveSubModules(@RequestBody SubModulesRequest subModulesRequest)
    {
        if(subModulesService.existsBySubModulesName(subModulesRequest.getName()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModuleNameAlReadyExistMessage()));
        }
        if(subModulesService.existsBySubModulesPrefix(subModulesRequest.getPrefix()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModulePrefixAlReadyExistMessage()));
        }
        if (!mainModulesService.existsByMainModuleId(subModulesRequest.getMain_module_Id()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getMainModulesNotExistCode(),
                    statusCodeBundle.getMainModuleNotExistsMessage()));
        }
        subModulesService.saveSubModules(subModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSaveSubModuleSuccessMessage()));

    }
    @PutMapping(EndpointURI.SUBMODULE)
    public ResponseEntity<Object> editSubModules(@RequestBody SubModulesRequest subModulesRequest)
    {
        if (!subModulesService.existsBySubModuleId(subModulesRequest.getId()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesNotExistCode(),
                    statusCodeBundle.getSubModuleNotExistsMessage()));
        }
        if (!mainModulesService.existsByMainModuleId(subModulesRequest.getMain_module_Id()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getMainModulesNotExistCode(),
                    statusCodeBundle.getMainModuleNotExistsMessage()));
        }
        if(subModulesService.isUpdateSubModuleNameExits(subModulesRequest.getName(),subModulesRequest.getId()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModuleNameAlReadyExistMessage()));
        }
        if(subModulesService.isUpdateSubModulePrefixExits(subModulesRequest.getPrefix(),subModulesRequest.getId()))
        {
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getSubModulesAlReadyExistCode(),
                    statusCodeBundle.getSubModulePrefixAlReadyExistMessage()));
        }

        subModulesService.saveSubModules(subModulesRequest);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getUpdateSubModuleSuccessMessage()));

    }

}
