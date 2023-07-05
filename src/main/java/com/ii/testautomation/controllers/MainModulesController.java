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
import com.ii.testautomation.utils.Utils;
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

    @GetMapping(EndpointURI.MAINMODULE_BY_NAME)
    public ResponseEntity<Object> getMainModulesByName(@RequestParam("name") String name)
    {
        if (!mainModulesService.isExistMainModulesName(name))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),
                    statusCodeBundle.getMainModulesNotExistCode(),statusCodeBundle.getMainModuleNotExistsMessage()));

        return ResponseEntity.ok(new ContentResponse<>(Constants.MAINMODULES,mainModulesService.getByMainModulesName(name),
                RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getSuccessViewAllMessage()));
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

    @PostMapping("/bulkIns")
    public ResponseEntity<Object> bulkSave(@RequestParam("file") MultipartFile file) throws IOException
    {
        List<Integer> Null_Value_RowNumbers = new ArrayList<>();
        List<Integer> Name_Already_Exist_RowNumbers = new ArrayList<>();
        List<Integer> Prefix_Already_Exist_RowNumbers = new ArrayList<>();
        List<Integer> ModulesId_NotFound_RowNumbers = new ArrayList<>();
        List<Integer> Correct_RowNumbers = new ArrayList<>();
        Map<String,List<Integer>> myErrorListMap = new HashMap<>();
        List mainmodulesList = new ArrayList<>();
        try
        {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet)
            {
                MainModulesRequest mainModulesRequest = new MainModulesRequest();
                if (row.getRowNum() == 0) continue;
                // Asigning the Cell Values
                Cell name = row.getCell(0);
                Cell prefix = row.getCell(1);
                Cell moduleId = row.getCell(2);

                Correct_RowNumbers.add(row.getRowNum()+1);
                myErrorListMap.put("Correct Row Numbers",Correct_RowNumbers);

                if (name == null || name.getCellType() == CellType.BLANK || prefix == null || prefix.getCellType() == CellType.BLANK || moduleId == null || moduleId.getCellType() == CellType.BLANK)
                {
                    Null_Value_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Identified Null Values in following Row Numbers", Null_Value_RowNumbers);

                }
                // Checking Validation
                if (!mainModulesService.isExistModulesId(Math.round(moduleId.getNumericCellValue())))
                {
                    ModulesId_NotFound_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Given Module Ids Not Found in following Row Numbers", ModulesId_NotFound_RowNumbers);

                }
                if (mainModulesService.isExistMainModulesName(name.getStringCellValue()))
                {

                    Name_Already_Exist_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Given Names Already Exist in following Row Numbers", Name_Already_Exist_RowNumbers);

                }
                if (mainModulesService.isExistPrefix(prefix.getStringCellValue()))
                {

                    Prefix_Already_Exist_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Given Prefixes Already Exist in following Row Numbers", Prefix_Already_Exist_RowNumbers);

                }

                //mainModulesRequest.setModuleId(Math.round(moduleId.getNumericCellValue()));
                //mainModulesRequest.setName(name.getStringCellValue());
                //mainModulesRequest.setPrefix(prefix.getStringCellValue());
                //mainmodulesList.add(mainModulesRequest);

            }



        } catch (Exception e){}

        return ResponseEntity.ok(myErrorListMap);
    }
    public void processfile(List<MainModulesRequest> mainModulesRequestList)
    {
        for (MainModulesRequest mainModulesRequest : mainModulesRequestList)
        {
            if (!Utils.isNotNullAndEmpty(mainModulesRequest.getName()))
                //return mainModulesRequestList.indexOf(mainModulesRequest);
                continue;
            if (!Utils.isNotNullAndEmpty(mainModulesRequest.getPrefix()))
                //return mainModulesRequestList.indexOf(mainModulesRequest);
                continue;
            if (!Utils.isNotNullAndEmpty(mainModulesRequest.getModuleId().toString()))
                //return mainModulesRequestList.indexOf(mainModulesRequest);
                continue;
            if (!mainModulesService.isExistModulesId(mainModulesRequest.getModuleId()))
                //return mainModulesRequestList.indexOf(mainModulesRequest);
                continue;
            if (mainModulesService.isExistMainModulesName(mainModulesRequest.getName()))
                //return mainModulesRequestList.indexOf(mainModulesRequest);
                continue;
            if (mainModulesService.isExistPrefix(mainModulesRequest.getPrefix()))
                //return mainModulesRequestList.indexOf(mainModulesRequest);
                continue;
            mainModulesService.saveMainModules(mainModulesRequest);

        }

    }


}

        /*
        List<Integer> Null_Value_RowNumbers = new ArrayList<>();
        List<Integer> Name_Already_Exist_RowNumbers = new ArrayList<>();
        List<Integer> Prefix_Already_Exist_RowNumbers = new ArrayList<>();
        List<Integer> ModulesId_NotFound_RowNumbers = new ArrayList<>();
        List<Integer> Correct_RowNumbers = new ArrayList<>();
        Map<String,List<Integer>> myErrorListMap = new HashMap<>();
        try
        {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet)
            {
                MainModulesRequest mainModulesRequest = new MainModulesRequest();
                if (row.getRowNum() == 0) continue;
                // Asigning the Cell Values
                Cell name = row.getCell(0);
                Cell prefix = row.getCell(1);
                Cell moduleId = row.getCell(2);


                // checking the Excel Sheet
                if (name == null || name.getCellType() == CellType.BLANK || prefix == null || prefix.getCellType() == CellType.BLANK || moduleId == null || moduleId.getCellType() == CellType.BLANK)
                {
                    Null_Value_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Identified Null Values in following Row Numbers", Null_Value_RowNumbers);

                }
                // Checking Validation
                if (!mainModulesService.isExistModulesId(Math.round(moduleId.getNumericCellValue())))
                {
                    ModulesId_NotFound_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Given Module Ids Not Found in following Row Numbers", ModulesId_NotFound_RowNumbers);

                }
                if (mainModulesService.isExistMainModulesName(name.getStringCellValue()))
                {
                    Name_Already_Exist_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Given Names Already Exist in following Row Numbers", Name_Already_Exist_RowNumbers);

                }
                if (mainModulesService.isExistPrefix(prefix.getStringCellValue()))
                {
                    Prefix_Already_Exist_RowNumbers.add(row.getRowNum() + 1);
                    myErrorListMap.put("Given Prefixes Already Exist in following Row Numbers", Prefix_Already_Exist_RowNumbers);

                }

                Correct_RowNumbers.add(row.getRowNum());
                myErrorListMap.put("Correct Row Numbers",Correct_RowNumbers);


                mainModulesRequest.setModuleId(Math.round(moduleId.getNumericCellValue()));
                mainModulesRequest.setName(name.getStringCellValue());
                mainModulesRequest.setPrefix(prefix.getStringCellValue());
                mainModulesService.saveMainModules(mainModulesRequest);


            }

                } catch (Exception e){throw e;}
            return ResponseEntity.ok(file.getOriginalFilename());
        }



        try {
            List<MainModulesRequest> mainModulesRequestList = new ArrayList<>();

            if (file.getContentType().matches(".csv"))
            {
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                for (CSVRecord csvRecord : csvRecords) {
                    MainModulesRequest mainModulesRequest = new MainModulesRequest();
                    mainModulesRequest.setName(csvRecord.get("name"));
                    mainModulesRequest.setPrefix(csvRecord.get("prefix"));
                    mainModulesRequest.setModuleId(Long.parseLong(csvRecord.get("moduleId")));
                    mainModulesRequestList.add(mainModulesRequest);
                }
                processfile(mainModulesRequestList);
                return ResponseEntity.ok("CSV OK");
            }

            if (file.getOriginalFilename().endsWith(".xlsx")) {

                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    MainModulesRequest mainModulesRequest = new MainModulesRequest();
                    if (row.getRowNum() == 0) continue;

                    mainModulesRequest.setModuleId(Math.round(row.getCell(2).getNumericCellValue()));
                    mainModulesRequest.setName(row.getCell(0).getStringCellValue());
                    mainModulesRequest.setPrefix(row.getCell(1).getStringCellValue());
                    mainModulesRequestList.add(mainModulesRequest);

                }
                processfile(mainModulesRequestList);
                return ResponseEntity.ok("Excel ok");

            }
        } catch (Exception e) {
            throw e;
        }
}
*/
