package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.search.MainModuleSearch;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.entities.QMainModules;
import com.ii.testautomation.repositories.MainModulesRepository;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.SubModulesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.MainModulesService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainModulesServiceImp implements MainModulesService
{
    @Autowired
    private MainModulesRepository mainModulesRepository;
    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private SubModulesRepository subModulesRepository;

    // CRUD
    @Override
    public void saveMainModules(MainModulesRequest mainModulesRequest)
    {
        MainModules mainModules = new MainModules();
        Modules modules1 = modulesRepository.findById(mainModulesRequest.getModuleId()).get();
        mainModules.setModules(modules1);
        BeanUtils.copyProperties(mainModulesRequest,mainModules);
        mainModulesRepository.save(mainModules);
    }
    public void deleteMainModules(Long id)
    {
        mainModulesRepository.deleteById(id);
    }
    @Override
    public MainModulesResponse getByMainModulesId(Long id)
    {
        MainModulesResponse mainModulesResponse = new MainModulesResponse();
        MainModules mainModules = mainModulesRepository.findById(id).get();
        mainModulesResponse.setModules(mainModules.getModules());
        BeanUtils.copyProperties(mainModules,mainModulesResponse);
        return mainModulesResponse;
    }
    @Override
    public List<MainModulesResponse> getMainModulesByModuleId(Long id)
    {
        List<MainModulesResponse> mainModulesResponseList = new ArrayList<>();
        List<MainModules> mainModulesList = mainModulesRepository.findAllByModulesId(id);

        for (MainModules mainModules : mainModulesList)
        {
            MainModulesResponse mainModulesResponse =new MainModulesResponse();
            BeanUtils.copyProperties(mainModules,mainModulesResponse);
            mainModulesResponseList.add(mainModulesResponse);
        }

        return mainModulesResponseList;
    }
    @Override
    public List<MainModulesResponse> getByMainModulesName(String name)
    {
        List<MainModulesResponse> mainModulesResponseList = new ArrayList<>();
        List<MainModules> mainModulesList = mainModulesRepository.findAllByNameIgnoreCase(name);

        for (MainModules mainModules: mainModulesList)
        {
            MainModulesResponse mainModulesResponse = new MainModulesResponse();
            BeanUtils.copyProperties(mainModules,mainModulesResponse);
            mainModulesResponseList.add(mainModulesResponse);
        }

        return mainModulesResponseList;
    }
    @Override

    // Search
    public List<MainModulesResponse> SearchMainModulesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, MainModuleSearch mainModuleSearch)
    {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(mainModuleSearch.getName()))
        {
            booleanBuilder.and(QMainModules.mainModules.name.containsIgnoreCase(mainModuleSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(mainModuleSearch.getPrefix()))
        {
            booleanBuilder.and(QMainModules.mainModules.prefix.containsIgnoreCase(mainModuleSearch.getPrefix()));
        }

        List<MainModulesResponse> mainModulesResponseList = new ArrayList<>();
        Page<MainModules> mainModulesPage = mainModulesRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(mainModulesPage.getTotalElements());
        pagination.setPageSize(mainModulesPage.getTotalPages());
        for (MainModules mainModules : mainModulesPage)
        {
            MainModulesResponse mainModulesResponse = new MainModulesResponse();
            BeanUtils.copyProperties(mainModules, mainModulesResponse);
            mainModulesResponseList.add(mainModulesResponse);
        }
        return mainModulesResponseList;
    }

    // Checking Functions
    @Override
    public boolean isExistModulesId(Long id)
    {
        return modulesRepository.existsById(id);
    }
    @Override
    public boolean isExistMainModulesName(String name)
    {
        return mainModulesRepository.existsByNameIgnoreCase(name);
    }
    @Override
    public boolean isExistPrefix(String prefix)
    {
        return mainModulesRepository.existsByPrefix(prefix);
    }
    @Override
    public boolean isExistMainModulesId(Long id)
    {
        return mainModulesRepository.existsById(id);
    }
    @Override
    public boolean isExistsSubmodulesByMainModule(Long id)
    {
        return subModulesRepository.existsByMainModuleId(id);
    }
    @Override
    public boolean existsMainModuleByModuleId(Long id) {return mainModulesRepository.existsByModulesId(id);}

    @Override
    public boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId) {
        return mainModulesRepository.existsByNameIgnoreCaseAndIdNot(mainModuleName, mainModuleId);
    }
    @Override
    public boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId) {
        return mainModulesRepository.existsByPrefixIgnoreCaseAndIdNot(mainModuleprefix,mainModuleId);
    }

    // Bulk Import
    @Override
    public boolean hasExcelFormat(MultipartFile multipartFile) {
        try {
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            workbook.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public List<MainModulesRequest> csvProcess(InputStream inputStream) {
        List<MainModulesRequest> mainModulesRequestList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords)
            {
                MainModulesRequest MainModulesRequest = new MainModulesRequest();
                MainModulesRequest.setModuleId(Long.parseLong(csvRecord.get("ModuleId")));
                MainModulesRequest.setPrefix(csvRecord.get("prefix"));
                MainModulesRequest.setName(csvRecord.get("name"));
                mainModulesRequestList.add(MainModulesRequest);
            }

        } catch (IOException e)
        {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return mainModulesRequestList;
    }
    @Override
    public List<MainModulesRequest> excelProcess(MultipartFile multipartFile) {
        List<MainModulesRequest> mainModulesRequestList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet)
            {
                if (row.getRowNum() == 0) continue;
                MainModulesRequest MainModulesRequest = new MainModulesRequest();
                MainModulesRequest.setModuleId(getLongCellValue(row.getCell(columnMap.get("module_id"))));
                MainModulesRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                MainModulesRequest.setPrefix(getStringCellValue(row.getCell(columnMap.get("prefix"))));
                mainModulesRequestList.add(MainModulesRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return mainModulesRequestList;
    }
    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value)
    {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }
    private Long getLongCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        cell.setCellType(CellType.NUMERIC);
        return (long) cell.getNumericCellValue();
    }
    private Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();

        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue().toLowerCase();
            int columnIndex = cell.getColumnIndex();
            columnMap.put(cellValue, columnIndex);
        }

        return columnMap;
    }
}

