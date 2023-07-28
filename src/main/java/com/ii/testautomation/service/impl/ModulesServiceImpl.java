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
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("ALL")
@Service
public class ModulesServiceImpl implements ModulesService {

    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void saveModule(ModulesRequest modulesRequest) {
        Modules modules = new Modules();
        Project project = new Project();
        project.setId(modulesRequest.getProject_id());
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
    public boolean existsByModulesId(Long id)
    {
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
    public Map<Integer,ModulesRequest> csvToModulesRequest(InputStream inputStream) {
        Map<Integer,ModulesRequest> modulesRequestsList = new HashMap<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {

                ModulesRequest modulesRequest = new ModulesRequest();
                modulesRequest.setName(csvRecord.get("name"));
                modulesRequest.setPrefix(csvRecord.get("prefix"));
                if(!csvRecord.get("project_id").isEmpty()) {
                    modulesRequest.setProject_id(Long.parseLong(csvRecord.get("project_id")));
                }else{
                    modulesRequest.setProject_id(null);
                }
                modulesRequestsList.put(Math.toIntExact(csvRecord.getRecordNumber()+1),modulesRequest);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return modulesRequestsList;
    }

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
    public Map<Integer,ModulesRequest> excelToModuleRequest(MultipartFile multipartFile) {
        Map<Integer,ModulesRequest> modulesRequestList = new HashMap<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                ModulesRequest modulesRequest = new ModulesRequest();
                modulesRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                modulesRequest.setPrefix(getStringCellValue(row.getCell(columnMap.get("prefix"))));
                modulesRequest.setProject_id(getLongCellValue(row.getCell(columnMap.get("project_id"))));
                modulesRequestList.put(row.getRowNum()+1, modulesRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return modulesRequestList;
    }

    @Override
    public boolean isExcelHeaderMatch(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            String[] actualHeaders = new String[headerRow.getLastCellNum()];
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                actualHeaders[i] = cell.getStringCellValue().toLowerCase();
            }
            String[] expectedHeader = {"name", "prefix", "project_id"};
            Set<String> expectedHeaderSet = new HashSet<>(Arrays.asList(expectedHeader));
            Set<String> actualHeaderSet = new HashSet<>(Arrays.asList(actualHeaders));
            return expectedHeaderSet.equals(actualHeaderSet);
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean isCSVHeaderMatch(MultipartFile multipartFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line = reader.readLine();
            String[] actualHeaders = line.split(",");
            for (int i = 0; i < actualHeaders.length; i++) {
                actualHeaders[i] = actualHeaders[i].toLowerCase();
            }
            String[] expectedHeader = {"name", "prefix", "project_id"};
            Set<String> expectedHeaderSet = new HashSet<>(Arrays.asList(expectedHeader));
            Set<String> actualHeaderSet = new HashSet<>(Arrays.asList(actualHeaders));
            return expectedHeaderSet.equals(actualHeaderSet);
        } catch (Exception e) {
            return false;
        }
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

    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }
}
