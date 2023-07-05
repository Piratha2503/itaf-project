package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.ModulesRequest;
//import com.ii.testautomation.dto.request.ProjectRequest;
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
import com.opencsv.CSVParser;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
//import org.apache.commons.csv.CSVParser;
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
    public List<ModulesResponse> multiSearchModules(Pageable pageable, PaginatedContentResponse.Pagination pagination,String searchTerm, ModuleSearch moduleSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(moduleSearch.getModuleName())) {

            booleanBuilder.and(QModules.modules.name.likeIgnoreCase("%" + searchTerm + "%"));
            //booleanBuilder.and(QModules.modules.name.eq(moduleSearch.getModuleName()));
        }
        if (Utils.isNotNullAndEmpty(moduleSearch.getModulePrefix())) {
           // booleanBuilder.and(QModules.modules.prefix.eq(moduleSearch.getModulePrefix()));
            booleanBuilder.and(QModules.modules.prefix.likeIgnoreCase("%" + searchTerm + "%"));
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

//    @Override
//    public boolean hasCsvFormat(MultipartFile multipartFile) {
//        String expectedContentType = "text/csv";
//        String actualContentType = multipartFile.getContentType();
//        return expectedContentType.equals(actualContentType);
//    }

//    @Override
//    public boolean hasExcelFormat(MultipartFile multipartFile) {
//        try {
//            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
//            workbook.close();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    @Override
//    public List<ModulesRequest> csvToModulesRequest(InputStream inputStream) {
//            List<ModulesRequest> modulesRequestsList = new ArrayList<>();
//            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//                 //CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
//                   // CSVParser csvParser=new CSVParser(fileReader,CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
//               // Iterable<CSVRecord> csvRecords = csvParser.getRecords();
//               // for (CSVRecord csvRecord:csvRecords) {
//
//                ModulesRequest modulesRequest = new ModulesRequest();
//                modulesRequest.setName(csvRecord.get("Name"));
//                modulesRequest.setPrefix(csvRecord.get("prefix"));
//                modulesRequest.setProjectId(Long.parseLong(csvRecord.get("ProjectId")));
//                modulesRequestsList.add(modulesRequest);
//            }
//
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
//            }
//        return modulesRequestsList;
//    }


//    public List<ModulesRequest> excelToModulesRequest(InputStream inputStream) {
//        List<ModulesRequest> moduleRequestList = new ArrayList<>();
//        try {
//            Workbook workbook = WorkbookFactory.create(inputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            DataFormatter dataFormatter = new DataFormatter();
//            boolean firstLine = true;
//
//            Row headerRow = sheet.getRow(0);
//            Map<String, Integer> columnMap = getColumnMap(headerRow);
//
//            for (Row row : sheet) {
//                if (firstLine) {
//                    firstLine = false;
//                    continue;
//                }
//                ModulesRequest modulesRequest=new ModulesRequest();
//
//                Cell nameCell = row.getCell(columnMap.get("Name"));
//                Cell prefixCell = row.getCell(columnMap.get("Prefix"));
//                Cell projectIdCell = row.getCell(columnMap.get("ProjectId"));
//
//                modulesRequest.setPrefix(dataFormatter.formatCellValue(prefixCell));
//                modulesRequest.setProjectId(Long.parseLong(dataFormatter.formatCellValue(projectIdCell)));
//                modulesRequest.setName(dataFormatter.formatCellValue(nameCell));
//
//                moduleRequestList.add(modulesRequest);
//            }
//
//            workbook.close();
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
//        }
//        return moduleRequestList;
//    }

//    @Override
//    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
//        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
//        errorList.add(value);
//        errorMessages.put(key, errorList);
//    }
//    private Map<String, Integer> getColumnMap(Row headerRow) {
//        Map<String, Integer> columnMap = new HashMap<>();
//
//        for (Cell cell : headerRow) {
//            String cellValue = cell.getStringCellValue();
//            columnMap.put(cellValue, cell.getColumnIndex());
//        }
//
//        return columnMap;
//    }



}
