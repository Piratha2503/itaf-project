package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestCaseRequest;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.dto.search.TestCaseSearch;
import com.ii.testautomation.entities.QTestCases;
import com.ii.testautomation.entities.QMainModules;
import com.ii.testautomation.entities.SubModules;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.repositories.SubModulesRepository;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestCasesService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
public class TestCasesServiceImpl implements TestCasesService {
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Autowired
    private SubModulesRepository subModulesRepository;

    @Override
    public void saveTestCase(TestCaseRequest testCaseRequest) {
        TestCases testCases = new TestCases();
        SubModules subModules = new SubModules();
        subModules.setId(testCaseRequest.getSubModuleId());
        testCases.setSubModule(subModules);
        BeanUtils.copyProperties(testCaseRequest, testCases);
        testCasesRepository.save(testCases);
    }

    @Override
    public boolean existsByTestCasesId(Long id) {
        if (id == null) {
            return false;
        }
        return testCasesRepository.existsById(id);
    }

    @Override
    public boolean existsByTestCasesName(String testCaseName, Long subModulesId) {
        Long projectId = subModulesRepository.findById(subModulesId).get().getMainModule().getModules().getProject().getId();
        return testCasesRepository.existsByNameIgnoreCaseAndSubModule_MainModule_Modules_Project_Id(testCaseName, projectId);
    }

    @Override
    public TestCaseResponse getById(Long id) {
        TestCaseResponse testCaseResponse = new TestCaseResponse();
        TestCases testCases = testCasesRepository.findById(id).get();
        testCaseResponse.setProjectId(testCases.getSubModule().getMainModule().getModules().getProject().getId());
        testCaseResponse.setProjectName(testCases.getSubModule().getMainModule().getModules().getProject().getName());
        testCaseResponse.setModuleId(testCases.getSubModule().getMainModule().getModules().getId());
        testCaseResponse.setSubmoduleId(testCases.getSubModule().getId());
        testCaseResponse.setMainModuleId(testCases.getSubModule().getMainModule().getId());
        testCaseResponse.setModuleName(testCases.getSubModule().getMainModule().getModules().getName());
        testCaseResponse.setMainModuleName(testCases.getSubModule().getMainModule().getName());
        testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
        BeanUtils.copyProperties(testCases, testCaseResponse);

        return testCaseResponse;
    }

    @Override
    public boolean isUpdateTestCaseNameExists(Long id, String name) {
        Long projectId = testCasesRepository.findById(id).get().getSubModule().getMainModule().getModules().getProject().getId();
        return testCasesRepository.existsByNameIgnoreCaseAndSubModule_MainModule_Modules_Project_IdAndIdNot(name, projectId, id);
    }

    @Override
    public List<TestCaseResponse> multiSearchTestCase(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestCaseSearch testCaseSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QTestCases qTestCases = QTestCases.testCases;
        QMainModules qMainModule= QMainModules.mainModules;
        if (Utils.isNotNullAndEmpty(testCaseSearch.getName())) {
            booleanBuilder.and(qTestCases.name.containsIgnoreCase(testCaseSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(testCaseSearch.getSubModuleName())) {
            booleanBuilder.and(qTestCases.subModule.name.containsIgnoreCase(testCaseSearch.getSubModuleName()));
        }
        if (Utils.isNotNullAndEmpty(testCaseSearch.getMainModuleName())) {
            booleanBuilder.and(qTestCases.subModule.mainModule.name.containsIgnoreCase(testCaseSearch.getMainModuleName()));
        }

        if (Utils.isNotNullAndEmpty(testCaseSearch.getModuleName())) {

            booleanBuilder.and(qMainModule.modules.name.containsIgnoreCase(testCaseSearch.getModuleName()));

        }
        if (Utils.isNotNullAndEmpty(testCaseSearch.getProjectName())) {
            booleanBuilder.and(qMainModule.modules.project.name.containsIgnoreCase(testCaseSearch.getProjectName()));
        }
        List<TestCaseResponse> testCaseResponseList = new ArrayList<>();
        Page<TestCases> testCasesPage = testCasesRepository.findAll(booleanBuilder, pageable);
        pagination.setTotalRecords(testCasesPage.getTotalElements());
        pagination.setPageSize(testCasesPage.getTotalPages());
        for (TestCases testCases : testCasesPage) {
            TestCaseResponse testCaseResponse = new TestCaseResponse();
            testCaseResponse.setProjectId(testCases.getSubModule().getMainModule().getModules().getProject().getId());
            testCaseResponse.setProjectName(testCases.getSubModule().getMainModule().getModules().getProject().getName());
            testCaseResponse.setModuleId(testCases.getSubModule().getMainModule().getModules().getId());
            testCaseResponse.setSubmoduleId(testCases.getSubModule().getId());
            testCaseResponse.setMainModuleId(testCases.getSubModule().getMainModule().getId());
            testCaseResponse.setModuleName(testCases.getSubModule().getMainModule().getModules().getName());
            testCaseResponse.setMainModuleName(testCases.getSubModule().getMainModule().getName());
            testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
            BeanUtils.copyProperties(testCases, testCaseResponse);
            testCaseResponseList.add(testCaseResponse);
        }
        return testCaseResponseList;
    }
    @Override
    public List<TestCaseResponse> getAllTestCaseBySubModuleId(Long subModuleId) {
        List<TestCaseResponse> testCaseResponseList = new ArrayList<>();
        List<TestCases> testCasesList = testCasesRepository.findAllTestCasesBySubModuleId(subModuleId);
        for (TestCases testCases : testCasesList) {
            TestCaseResponse testCaseResponse = new TestCaseResponse();
            testCaseResponse.setProjectId(testCases.getSubModule().getMainModule().getModules().getProject().getId());
            testCaseResponse.setProjectName(testCases.getSubModule().getMainModule().getModules().getProject().getName());
            testCaseResponse.setModuleId(testCases.getSubModule().getMainModule().getModules().getId());
            testCaseResponse.setSubmoduleId(testCases.getSubModule().getId());
            testCaseResponse.setMainModuleId(testCases.getSubModule().getMainModule().getId());
            testCaseResponse.setModuleName(testCases.getSubModule().getMainModule().getModules().getName());
            testCaseResponse.setMainModuleName(testCases.getSubModule().getMainModule().getName());
            testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
            BeanUtils.copyProperties(testCases, testCaseResponse);
            testCaseResponseList.add(testCaseResponse);
        }
        return testCaseResponseList;
    }
    @Override
    public void DeleteTestCaseById(Long id) {
        testCasesRepository.deleteById(id);
    }

    @Override
    public boolean existsBySubModuleId(Long subModuleId) {
        return testCasesRepository.existsBySubModuleId(subModuleId);
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
    public Map<Integer, TestCaseRequest> csvToTestCaseRequest(InputStream inputStream) {
        Map<Integer, TestCaseRequest> testCaseRequestList = new HashMap<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                TestCaseRequest testCaseRequest = new TestCaseRequest();
                testCaseRequest.setDescription(csvRecord.get("description"));
                testCaseRequest.setName(csvRecord.get("name"));
                if (!csvRecord.get("submodule_id").isEmpty()) {
                    testCaseRequest.setSubModuleId(Long.parseLong(csvRecord.get("submodule_id")));
                } else {
                    testCaseRequest.setSubModuleId(null);
                }
                testCaseRequestList.put(Math.toIntExact(csvRecord.getRecordNumber() + 1), testCaseRequest);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return testCaseRequestList;
    }

    @Override
    public Map<Integer, TestCaseRequest> excelToTestCaseRequest(MultipartFile multipartFile) {
        Map<Integer, TestCaseRequest> testCaseRequestList = new HashMap<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                TestCaseRequest testCaseRequest = new TestCaseRequest();
                testCaseRequest.setDescription(getStringCellValue(row.getCell(columnMap.get("description"))));
                testCaseRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                testCaseRequest.setSubModuleId(getLongCellValue(row.getCell(columnMap.get("submodule_id"))));
                testCaseRequestList.put(row.getRowNum() + 1, testCaseRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to Parse Excel File: " + e.getMessage());
        }
        return testCaseRequestList;
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
            String[] expectedHeader = {"description", "name", "submodule_id"};
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
            String[] expectedHeader = {"description", "name", "submodule_id"};
            Set<String> expectedHeaderSet = new HashSet<>(Arrays.asList(expectedHeader));
            Set<String> actualHeaderSet = new HashSet<>(Arrays.asList(actualHeaders));
            return expectedHeaderSet.equals(actualHeaderSet);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }
    @Override
    public List<TestCaseResponse> getAllTestcasesByProjectId(Long projectId) {
        List<TestCaseResponse> testCaseResponseList = new ArrayList<>();
        List<TestCases> testCasesList = testCasesRepository.findBySubModule_MainModule_Modules_Project_Id(projectId);
        for (TestCases testCases : testCasesList) {
            TestCaseResponse testCaseResponse = new TestCaseResponse();
            testCaseResponse.setProjectId(testCases.getSubModule().getMainModule().getModules().getProject().getId());
            testCaseResponse.setProjectName(testCases.getSubModule().getMainModule().getModules().getProject().getName());
            testCaseResponse.setModuleId(testCases.getSubModule().getMainModule().getModules().getId());
            testCaseResponse.setSubmoduleId(testCases.getSubModule().getId());
            testCaseResponse.setMainModuleId(testCases.getSubModule().getMainModule().getId());
            testCaseResponse.setModuleName(testCases.getSubModule().getMainModule().getModules().getName());
            testCaseResponse.setMainModuleName(testCases.getSubModule().getMainModule().getName());
            testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
            BeanUtils.copyProperties(testCases, testCaseResponse);
            testCaseResponseList.add(testCaseResponse);
        }
        return testCaseResponseList;
    }

    @Override
    public List<TestCaseResponse> getAllTestCasesByModuleId(Long moduleId) {
        List<TestCaseResponse> testCaseResponseList=new ArrayList<>();
        List<TestCases> testCasesList=testCasesRepository.findBySubModule_MainModule_Modules_Id(moduleId);
        for (TestCases testCases:testCasesList) {
            TestCaseResponse testCaseResponse=new TestCaseResponse();
            testCaseResponse.setProjectId(testCases.getSubModule().getMainModule().getModules().getProject().getId());
            testCaseResponse.setProjectName(testCases.getSubModule().getMainModule().getModules().getProject().getName());
            testCaseResponse.setModuleId(testCases.getSubModule().getMainModule().getModules().getId());
            testCaseResponse.setModuleName(testCases.getSubModule().getMainModule().getModules().getName());
            testCaseResponse.setMainModuleId(testCases.getSubModule().getMainModule().getId());
            testCaseResponse.setMainModuleName(testCases.getSubModule().getMainModule().getName());
            testCaseResponse.setSubmoduleId(testCases.getSubModule().getId());
            testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
            BeanUtils.copyProperties(testCases,testCaseResponse);
            testCaseResponseList.add(testCaseResponse);

        }
        return testCaseResponseList;
    }

    @Override
    public List<TestCaseResponse> getAllTestCasesByMainModuleId(Long MainModuleId) {
        List<TestCaseResponse> testCaseResponseList=new ArrayList<>();
        List<TestCases> testCasesList=testCasesRepository.findBySubModule_MainModule_Id(MainModuleId);
        for (TestCases testCases:testCasesList) {
            TestCaseResponse testCaseResponse=new TestCaseResponse();
            testCaseResponse.setProjectId(testCases.getSubModule().getMainModule().getModules().getProject().getId());
            testCaseResponse.setProjectName(testCases.getSubModule().getMainModule().getModules().getProject().getName());
            testCaseResponse.setModuleId(testCases.getSubModule().getMainModule().getModules().getId());
            testCaseResponse.setModuleName(testCases.getSubModule().getMainModule().getModules().getName());
            testCaseResponse.setMainModuleId(testCases.getSubModule().getMainModule().getId());
            testCaseResponse.setMainModuleName(testCases.getSubModule().getMainModule().getName());
            testCaseResponse.setSubmoduleId(testCases.getSubModule().getId());
            testCaseResponse.setSubModuleName(testCases.getSubModule().getName());
            BeanUtils.copyProperties(testCases,testCaseResponse);
            testCaseResponseList.add(testCaseResponse);

        }
        return testCaseResponseList;
    }

    @Override
    public boolean existsTestCaseByProjectId(Long projectId) {
        return testCasesRepository.existsBySubModule_MainModule_Modules_Project_id(projectId);
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
