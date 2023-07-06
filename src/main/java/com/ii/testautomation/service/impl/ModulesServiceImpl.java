package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.ModulesRequest;
import com.ii.testautomation.dto.response.ModulesResponse;
import com.ii.testautomation.dto.search.ModuleSearch;
import com.ii.testautomation.entities.Modules;
import com.ii.testautomation.entities.Project;
import com.ii.testautomation.entities.QModules;
import com.ii.testautomation.repositories.ModulesRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ModulesService;
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
public class ModulesServiceImpl implements ModulesService
{

    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void saveModule(ModulesRequest modulesRequest) {
        Modules modules = new Modules();
        Project project = new Project();
        project.setId(modulesRequest.getProjectId());
        modules.setProject(project);
        BeanUtils.copyProperties(modulesRequest, modules);
        modulesRepository.save(modules);
    }

    @Override
    public boolean isModuleExistsByName(String name) {
        return modulesRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean isModuleExistsByPrefix(String prefix) {
        return modulesRepository.existsByPrefixIgnoreCase(prefix);
    }

    @Override
    public boolean existsByModulesId(Long id) {
        return modulesRepository.existsById(id);
    }

    @Override
    public boolean isUpdateModuleNameExists(String name, Long id) {
        return modulesRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public boolean isUpdateModulePrefixExists(String prefix, Long id) {
        return modulesRepository.existsByPrefixIgnoreCaseAndIdNot(prefix, id);
    }

    @Override
    public List<ModulesResponse> multiSearchModules(Pageable pageable, PaginatedContentResponse.Pagination pagination, ModuleSearch moduleSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(moduleSearch.getModuleName())) {
            booleanBuilder.and(QModules.modules.name.containsIgnoreCase(moduleSearch.getModuleName()));
        }
        if (Utils.isNotNullAndEmpty(moduleSearch.getModulePrefix())) {
            booleanBuilder.and(QModules.modules.prefix.containsIgnoreCase(moduleSearch.getModulePrefix()));
          //  booleanBuilder.and(QModules.modules.prefix.likeIgnoreCase("%" + searchTerm + "%"));
        }
        List<ModulesResponse> modulesResponseList = new ArrayList<>();
        Page<Modules> modulesPage = modulesRepository.findAll(booleanBuilder, pageable);
        pagination.setTotalRecords(modulesPage.getTotalElements());
        pagination.setPageSize(modulesPage.getTotalPages());
        for (Modules modules : modulesPage) {
            ModulesResponse modulesResponse = new ModulesResponse();
            modulesResponse.setProjectId(modules.getProject().getId());
            modulesResponse.setProjectName(modules.getProject().getName());
            BeanUtils.copyProperties(modules, modulesResponse);
            modulesResponseList.add(modulesResponse);

        }

        return modulesResponseList;
    }

    @Override
    public ModulesResponse getModuleById(Long id) {
        ModulesResponse modulesResponse = new ModulesResponse();
        Modules module = modulesRepository.findById(id).get();
        modulesResponse.setProjectId(module.getProject().getId());
        modulesResponse.setProjectName(module.getProject().getName());
        BeanUtils.copyProperties(module, modulesResponse);
        return modulesResponse;
    }

    @Override
    public List<ModulesResponse> getAllModuleByProjectId(Long projectId) {
        List<ModulesResponse> modulesResponseList = new ArrayList<>();
        List<Modules> modulesList = modulesRepository.findAllModulesByProjectId(projectId);
        for (Modules module : modulesList) {
            ModulesResponse modulesResponse = new ModulesResponse();
            modulesResponse.setProjectId(module.getProject().getId());
            modulesResponse.setProjectName(module.getProject().getName());
            BeanUtils.copyProperties(module, modulesResponse);
            modulesResponseList.add(modulesResponse);
        }
        return modulesResponseList;
    }

    @Override
    public void deleteModuleById(Long id) {
        modulesRepository.deleteById(id);
    }

    @Override
    public boolean existsModuleByProjectId(Long projectId) {
        return modulesRepository.existsByProjectId(projectId);
    }




    @Override
    public List<ModulesRequest> csvToModulesRequest(InputStream inputStream) {
           List<ModulesRequest> modulesRequestsList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                for (CSVRecord csvRecord:csvRecords) {

                ModulesRequest modulesRequest = new ModulesRequest();
                modulesRequest.setName(csvRecord.get("name"));
                modulesRequest.setPrefix(csvRecord.get("prefix"));
                modulesRequest.setProjectId(Long.parseLong(csvRecord.get("projectId")));
                modulesRequestsList.add(modulesRequest);
            }

            } catch (IOException e) {
                throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
            }
        return modulesRequestsList;
    }
    @Override
    public boolean hasExcelFormat(MultipartFile multipartFile){
       try {
           Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
           workbook.close();
           return true;
       }catch(Exception e){
           return false;
        }
    }
    @Override
    public List<ModulesRequest> excelToModulesRequest(MultipartFile multipartFile){
        List<ModulesRequest> modulesRequestList=new ArrayList<>();
        try{
            Workbook workbook=new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet= workbook.getSheetAt(0);
            DataFormatter dataFormatter=new DataFormatter();
            Row headerRow= sheet.getRow(0);
            Map<String,Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet) {
                if(row.getRowNum()==0) continue;
                ModulesRequest modulesRequest=new ModulesRequest();
                modulesRequest.setName(row.getCell(0).getStringCellValue());
                modulesRequest.setPrefix(row.getCell(1).getStringCellValue());
                modulesRequest.setProjectId(getLongCellValue(row.getCell(2)));
                modulesRequestList.add(modulesRequest);
            }
          workbook.close();
        }catch (IOException e){
            throw new RuntimeException("Failed to Parse Excel File: "+ e.getMessage());
        }
        return modulesRequestList;
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


    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
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
