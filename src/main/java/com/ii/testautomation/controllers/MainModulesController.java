package com.ii.testautomation.controllers;
import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.response.bulkResponse.MainModulesBResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
public class MainModulesController {
    @Autowired
    private MainModulesService mainModulesService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

    @PostMapping(EndpointURI.MAINMODULE)
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
                statusCodeBundle.getSuccessMessageInsert()));
    }

    @DeleteMapping(EndpointURI.MAINMODULE_BY_ID)
    public ResponseEntity<Object> deleteMainModules(@PathVariable Long id) {
        if (!mainModulesService.isExistMainModulesId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getMainIdnotFound()));

        if (mainModulesService.isExistsSubmodulesByMainModule(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getFailureCode(),
                    statusCodeBundle.getIdAsignedwithanotherTable()));

        mainModulesService.deleteMainModules(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessMessageDelete()));
    }

    @GetMapping(EndpointURI.MAINMODULE_BY_ID)
    public ResponseEntity<Object> getMainModulesByMainModuleId(@PathVariable Long id) {
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
                statusCodeBundle.getSuccessViewAllMessage()));
    }

    @PutMapping(EndpointURI.MAINMODULE)
    public ResponseEntity<Object> updateMainModules(@RequestBody MainModulesRequest mainModulesRequest) {
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
                                                            MainModuleSearch mainModuleSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0l);

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES, mainModulesService.SearchMainModulesWithPagination(pageable, pagination, mainModuleSearch),
                RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));
    }

    @PostMapping("/bulkInsert")
    public ResponseEntity<Object> bulkSave(@RequestParam("file") MultipartFile file) throws IOException {

        List<Integer> Null_Value_RowNumbers = new ArrayList<>();
        List<Integer> Name_Already_Exist_RowNumbers = new ArrayList<>();
        List<Integer> Prefix_Already_Exist_RowNumbers = new ArrayList<>();
        List<Integer> ModulesId_NotFound_RowNumbers = new ArrayList<>();
        BaseResponse baseResponse = new BaseResponse(RequestStatus.SUCCESS.getStatus(),
                statusCodeBundle.getBulkImportCode(), statusCodeBundle.getBulkImportMessage());


        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                MainModulesRequest mainModulesRequest = new MainModulesRequest();
                if (row.getRowNum() == 0) continue;

                // Asigning the Cell Values
                Cell name = row.getCell(0);
                Cell prefix = row.getCell(1);
                Cell moduleId = row.getCell(2);

                // checking the Excel Sheet
                if (name == null || name.getCellType() == CellType.BLANK) {
                    Null_Value_RowNumbers.add(row.getRowNum() + 1);
                    continue;
                }
                if (prefix == null || prefix.getCellType() == CellType.BLANK) {
                    Null_Value_RowNumbers.add(row.getRowNum() + 1);
                    continue;
                }
                if (moduleId == null || moduleId.getCellType() == CellType.BLANK) {
                    Null_Value_RowNumbers.add(row.getRowNum() + 1);
                    continue;
                }

                // Convert double into Long
                Long moduleIds = Math.round(moduleId.getNumericCellValue());

                // Checking Validation
                if (!mainModulesService.isExistModulesId(moduleIds)) {
                    ModulesId_NotFound_RowNumbers.add(row.getRowNum() + 1);
                    continue;
                }
                if (mainModulesService.isExistMainModulesName(name.getStringCellValue())) {
                    Name_Already_Exist_RowNumbers.add(row.getRowNum() + 1);
                    continue;
                }
                if (mainModulesService.isExistPrefix(prefix.getStringCellValue())) {
                    Prefix_Already_Exist_RowNumbers.add(row.getRowNum() + 1);
                    continue;
                }
                // Set Entity fields
                mainModulesRequest.setName(name.getStringCellValue());
                mainModulesRequest.setPrefix(prefix.getStringCellValue());
                mainModulesRequest.setModuleId(moduleIds);

                // Send to save
                mainModulesService.saveMainModules(mainModulesRequest);
            }

            return ResponseEntity.ok(new MainModulesBResponse(baseResponse, Constants.SKIPPED_MSG,
                    Null_Value_RowNumbers, Name_Already_Exist_RowNumbers, Prefix_Already_Exist_RowNumbers, ModulesId_NotFound_RowNumbers));

        } catch (Exception e) {
        }

        return ResponseEntity.ok(new BaseResponse("Success", "20000", "Successfully Inserted"));
    }

}