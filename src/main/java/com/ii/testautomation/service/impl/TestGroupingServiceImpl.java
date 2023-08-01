package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.entities.QTestGrouping;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestTypes;
import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.repositories.TestGroupingRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.TestGroupingService;
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
import java.util.stream.Collectors;

@Service
public class TestGroupingServiceImpl implements TestGroupingService {
    @Autowired
    private TestGroupingRepository testGroupingRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;

    @Override
    public void saveTestGrouping(TestGroupingRequest testGroupingRequest) {
        TestGrouping testGrouping = new TestGrouping();
        List<TestCases> testCasesList = testGroupingRequest.getTestCaseId().stream()
                .map(testCaseId -> {
                    TestCases testCases = new TestCases();
                    testCases.setId(testCaseId);
                    return testCases;
                })
                .collect(Collectors.toList());
        testGrouping.setTestCases(testCasesList);
        TestTypes testTypes = new TestTypes();
        testTypes.setId(testGroupingRequest.getTestTypeId());
        testGrouping.setTestType(testTypes);
        BeanUtils.copyProperties(testGroupingRequest, testGrouping);
        testGroupingRepository.save(testGrouping);
    }
//    @Override
//    public boolean allTestCasesInSameProject(List<Long> testCaseIds, Long projectId) {
//        Set<Long> uniqueProjectIds = new HashSet<>();
//        for (Long testCaseId : testCaseIds) {
//            Long testCasesProjectId = testGroupingRepository.findById(testCaseId).get().getTestCases();
//            uniqueProjectIds.add(testCasesProjectId);
//        }
//
//        return uniqueProjectIds.size() == 1 && uniqueProjectIds.contains(projectId);
//    }
    @Override
    public boolean existsByTestGroupingName(String testGroupingName, Long testCaseId) {
        Long projectId = testCasesRepository.findById(testCaseId).get().getSubModule().getMainModule().getModules().getProject().getId();
        return testGroupingRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(testGroupingName, projectId, testCaseId);
    }

    @Override
    public boolean existsByTestGroupingId(Long testGroupingId) {
        return testGroupingRepository.existsById(testGroupingId);
    }

    @Override
    public boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testGroupingId) {
        for (TestCases testCases : testGroupingRepository.findById(testGroupingId).get().getTestCases()
        ) {
            Long projectId = testCases.getSubModule().getMainModule().getModules().getProject().getId();
            if (testGroupingRepository.existsByNameIgnoreCaseAndTestCases_SubModule_MainModule_Modules_Project_IdAndIdNot(testGroupingName, projectId, testGroupingId))
                return true;
        }
        return false;
    }

    @Override
    public TestGroupingResponse getTestGroupingById(Long testGroupingId) {
        TestGrouping testGrouping = testGroupingRepository.findById(testGroupingId).get();
        TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
        List<String> subModuleNames = new ArrayList<>();
        List<String> mainModuleNames = new ArrayList<>();
        List<String> moduleNames = new ArrayList<>();
        List<String> testCaseNames = testGrouping.getTestCases().stream()
                .map(TestCases::getName)
                .collect(Collectors.toList());
        for (TestCases testCase : testGrouping.getTestCases()) {
            subModuleNames.add(testCase.getSubModule().getName());
            mainModuleNames.add(testCase.getSubModule().getMainModule().getName());
            moduleNames.add(testCase.getSubModule().getMainModule().getModules().getName());
        }
        testGroupingResponse.setTestCasesName(testCaseNames);
        testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
        testGroupingResponse.setSubModuleName(subModuleNames);
        testGroupingResponse.setMainModuleName(mainModuleNames);
        testGroupingResponse.setModuleName(moduleNames);
        BeanUtils.copyProperties(testGrouping, testGroupingResponse);
        return testGroupingResponse;
    }

    @Override
    public List<TestGroupingResponse> getALlTestGroupingByTestCaseId(Long testCaseId) {
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestCasesId(testCaseId);
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        for (TestGrouping testGrouping : testGroupingList) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            List<String> subModuleNames = new ArrayList<>();
            List<String> mainModuleNames = new ArrayList<>();
            List<String> moduleNames = new ArrayList<>();
            List<String> testCaseNames = testGrouping.getTestCases().stream()
                    .map(TestCases::getName)
                    .collect(Collectors.toList());
            for (TestCases testCase : testGrouping.getTestCases()) {
                subModuleNames.add(testCase.getSubModule().getName());
                mainModuleNames.add(testCase.getSubModule().getMainModule().getName());
                moduleNames.add(testCase.getSubModule().getMainModule().getModules().getName());
            }
            testGroupingResponse.setTestCasesName(testCaseNames);
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(subModuleNames);
            testGroupingResponse.setMainModuleName(mainModuleNames);
            testGroupingResponse.setModuleName(moduleNames);
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public List<TestGroupingResponse> getALlTestGroupingByTestTypeId(Long testTypeId) {
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestTypeId(testTypeId);
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        for (TestGrouping testGrouping : testGroupingList) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            List<String> subModuleNames = new ArrayList<>();
            List<String> mainModuleNames = new ArrayList<>();
            List<String> moduleNames = new ArrayList<>();
            List<String> testCaseNames = testGrouping.getTestCases().stream()
                    .map(TestCases::getName)
                    .collect(Collectors.toList());
            for (TestCases testCase : testGrouping.getTestCases()) {
                subModuleNames.add(testCase.getSubModule().getName());
                mainModuleNames.add(testCase.getSubModule().getMainModule().getName());
                moduleNames.add(testCase.getSubModule().getMainModule().getModules().getName());
            }
            testGroupingResponse.setTestCasesName(testCaseNames);
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(subModuleNames);
            testGroupingResponse.setMainModuleName(mainModuleNames);
            testGroupingResponse.setModuleName(moduleNames);
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public List<TestGroupingResponse> getTestGroupingByProjectId(Long id) {
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        List<TestGrouping> testGroupingList = testGroupingRepository.findByTestCases_SubModule_MainModule_Modules_Project_Id(id);
        for (TestGrouping testGrouping : testGroupingList
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            List<String> subModuleNames = new ArrayList<>();
            List<String> mainModuleNames = new ArrayList<>();
            List<String> moduleNames = new ArrayList<>();
            List<String> testCaseNames = testGrouping.getTestCases().stream()
                    .map(TestCases::getName)
                    .collect(Collectors.toList());
            for (TestCases testCase : testGrouping.getTestCases()) {
                subModuleNames.add(testCase.getSubModule().getName());
                mainModuleNames.add(testCase.getSubModule().getMainModule().getName());
                moduleNames.add(testCase.getSubModule().getMainModule().getModules().getName());
            }
            testGroupingResponse.setTestCasesName(testCaseNames);
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(subModuleNames);
            testGroupingResponse.setMainModuleName(mainModuleNames);
            testGroupingResponse.setModuleName(moduleNames);
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }
    @Override
    public List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.name.containsIgnoreCase(testGroupingSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestCaseName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testCases.any().name.containsIgnoreCase(testGroupingSearch.getTestCaseName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestTypeName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testType.name.containsIgnoreCase(testGroupingSearch.getTestTypeName()));
        }
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        Page<TestGrouping> testGroupings = testGroupingRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(testGroupings.getTotalElements());
        pagination.setPageSize(testGroupings.getTotalPages());
        for (TestGrouping testGrouping : testGroupings) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            List<String> subModuleNames = new ArrayList<>();
            List<String> mainModuleNames = new ArrayList<>();
            List<String> moduleNames = new ArrayList<>();
            List<String> testCaseNames = testGrouping.getTestCases().stream()
                    .map(TestCases::getName)
                    .collect(Collectors.toList());
            for (TestCases testCase : testGrouping.getTestCases()) {
                subModuleNames.add(testCase.getSubModule().getName());
                mainModuleNames.add(testCase.getSubModule().getMainModule().getName());
                moduleNames.add(testCase.getSubModule().getMainModule().getModules().getName());
            }
            testGroupingResponse.setTestCasesName(testCaseNames);
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(subModuleNames);
            testGroupingResponse.setMainModuleName(mainModuleNames);
            testGroupingResponse.setModuleName(moduleNames);
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public void deleteTestGroupingById(Long testGroupingId) {
        testGroupingRepository.deleteById(testGroupingId);
    }

    @Override
    public boolean existsByTestCasesId(Long testCaseId) {
        return testGroupingRepository.existsByTestCasesId(testCaseId);
    }

    @Override
    public boolean existsByTestTypesId(Long testTypeId) {
        return testGroupingRepository.existsByTestTypeId(testTypeId);
    }

    @Override
    public boolean existsByProjectId(Long projectId) {
        return testGroupingRepository.existsByTestCases_SubModule_MainModule_Modules_ProjectId(projectId);
    }

    @Override
    public Map<Integer, TestGroupingRequest> csvToTestGroupingRequest(InputStream inputStream) {
        Map<Integer, TestGroupingRequest> testGroupingRequestList = new HashMap<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                TestGroupingRequest testGroupingRequest = new TestGroupingRequest();
                testGroupingRequest.setName(csvRecord.get("name"));
                if (!csvRecord.get("test_case_id").isEmpty()) {
                    List<Long> testCaseIds = Arrays.stream(csvRecord.get("testCaseId").split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    testGroupingRequest.setTestCaseId(testCaseIds);
                } else {
                    testGroupingRequest.setTestCaseId(null);
                }
                if (!csvRecord.get("test_type_id").isEmpty()) {
                    testGroupingRequest.setTestTypeId(Long.parseLong(csvRecord.get("test_type_id")));
                } else {
                    testGroupingRequest.setTestTypeId(null);
                }
                testGroupingRequestList.put(Math.toIntExact(csvRecord.getRecordNumber()) + 1, testGroupingRequest);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return testGroupingRequestList;
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
    public boolean hasCsvFormat(MultipartFile multipartFile) {
        String expectedContentType = "text/csv";
        String actualContentType = multipartFile.getContentType();
        return expectedContentType.equals(actualContentType);
    }

    @Override
    public Map<Integer, TestGroupingRequest> excelToTestGroupingRequest(MultipartFile multipartFile) {
        Map<Integer, TestGroupingRequest> testGroupingRequestList = new HashMap<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                TestGroupingRequest testGroupingRequest = new TestGroupingRequest();
                testGroupingRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                testGroupingRequest.setTestTypeId(getLongCellValue(row.getCell(columnMap.get("test_type_id"))));
                List<Long> testCaseIds = Arrays.stream(getStringCellValue(row.getCell(columnMap.get("testCaseId"))).split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                testGroupingRequest.setTestCaseId(testCaseIds);
                testGroupingRequestList.put(row.getRowNum() + 1, testGroupingRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return testGroupingRequestList;
    }

    @Override
    public boolean isExcelHeaderMatch(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            String[] actualHeaders = new String[headerRow.getLastCellNum()];
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                actualHeaders[i] = cell.getStringCellValue().toLowerCase();
            }
            String[] expectedHeader = {"name", "test_case_id", "test_type_id"};
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
            String[] expectedHeader = {"name", "test_case_id", "test_type_id"};
            Set<String> expectedHeaderSet = new HashSet<>(Arrays.asList(expectedHeader));
            Set<String> actualHeaderSet = new HashSet<>(Arrays.asList(actualHeaders));
            return expectedHeaderSet.equals(actualHeaderSet);
        } catch (Exception e) {
            return false;
        }
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
}
