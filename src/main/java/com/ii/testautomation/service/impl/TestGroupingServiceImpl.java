package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.TestGroupingRequest;
import com.ii.testautomation.dto.response.TestGroupingResponse;
import com.ii.testautomation.dto.search.TestGroupingSearch;
import com.ii.testautomation.entities.QTestGrouping;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestTypes;
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
import java.util.*;

@Service
public class TestGroupingServiceImpl implements TestGroupingService {
    @Autowired
    private TestGroupingRepository testGroupingRepository;

    @Override
    public void saveTestGrouping(TestGroupingRequest testGroupingRequest) {
        TestGrouping testGrouping = new TestGrouping();
        TestCases testCases = new TestCases();
        testCases.setId(testGroupingRequest.getTestCaseId());
        testGrouping.setTestCases(testCases);
        TestTypes testTypes = new TestTypes();
        testTypes.setId(testGroupingRequest.getTestTypeId());
        testGrouping.setTestType(testTypes);
        BeanUtils.copyProperties(testGroupingRequest, testGrouping);
        testGroupingRepository.save(testGrouping);
    }

    @Override
    public boolean existsByTestGroupingName(String testGroupingName) {
        return testGroupingRepository.existsByNameIgnoreCase(testGroupingName);
    }

    @Override
    public boolean existsByTestGroupingId(Long testGroupingId) {
        return testGroupingRepository.existsById(testGroupingId);
    }

    @Override
    public boolean isUpdateTestGroupingNameExits(String testGroupingName, Long testGroupingId) {
        return testGroupingRepository.existsByNameIgnoreCaseAndIdNot(testGroupingName, testGroupingId);
    }

    @Override
    public TestGroupingResponse getTestGroupingById(Long testGroupingId) {
        TestGrouping testGrouping = testGroupingRepository.findById(testGroupingId).get();
        TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
        testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
        testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
        testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
        testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
        testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModules().getName());
        BeanUtils.copyProperties(testGrouping, testGroupingResponse);
        return testGroupingResponse;
    }

    @Override
    public List<TestGroupingResponse> getALlTestGroupingByTestCaseId(Long testCaseId) {
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestCasesId(testCaseId);
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        for (TestGrouping testGrouping : testGroupingList
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
            testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModules().getName());
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public List<TestGroupingResponse> getALlTestGroupingByTestTypeId(Long testTypeId) {
        List<TestGrouping> testGroupingList = testGroupingRepository.findAllTestGroupingByTestTypeId(testTypeId);
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        for (TestGrouping testGrouping : testGroupingList
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
            testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModules().getName());
            BeanUtils.copyProperties(testGrouping, testGroupingResponse);
            testGroupingResponses.add(testGroupingResponse);
        }
        return testGroupingResponses;
    }

    @Override
    public List<TestGroupingResponse> multiSearchTestGrouping(Pageable pageable, PaginatedContentResponse.Pagination pagination, TestGroupingSearch testGroupingSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.name.eq(testGroupingSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestCaseName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testCases.name.containsIgnoreCase(testGroupingSearch.getTestCaseName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestTypeName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testType.name.containsIgnoreCase(testGroupingSearch.getTestTypeName()));
        }
        List<TestGroupingResponse> testGroupingResponses = new ArrayList<>();
        Page<TestGrouping> testGroupings = testGroupingRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(testGroupings.getTotalElements());
        pagination.setPageSize(testGroupings.getTotalPages());
        for (TestGrouping testGrouping : testGroupings
        ) {
            TestGroupingResponse testGroupingResponse = new TestGroupingResponse();
            testGroupingResponse.setTestCasesName(testGrouping.getTestCases().getName());
            testGroupingResponse.setTestTypesName(testGrouping.getTestType().getName());
            testGroupingResponse.setSubModuleName(testGrouping.getTestCases().getSubModule().getName());
            testGroupingResponse.setMainModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getName());
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModules().getName());
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
    public List<TestGroupingRequest> csvToTestGroupingRequest(InputStream inputStream) {
        List<TestGroupingRequest> testGroupingRequestList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                TestGroupingRequest testGroupingRequest = new TestGroupingRequest();
                testGroupingRequest.setName(csvRecord.get("name"));
                testGroupingRequest.setTestCaseId(Long.parseLong(csvRecord.get("test_case_id")));
                testGroupingRequest.setTestTypeId(Long.parseLong(csvRecord.get("test_type_id")));
                testGroupingRequestList.add(testGroupingRequest);
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
    public List<TestGroupingRequest> excelToTestGroupingRequest(MultipartFile multipartFile) {
        List<TestGroupingRequest> testGroupingRequestList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                TestGroupingRequest testGroupingRequest = new TestGroupingRequest();
                testGroupingRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                testGroupingRequest.setTestTypeId(getLongCellValue(row.getCell(columnMap.get("test_type_id"))));
                testGroupingRequest.setTestCaseId(getLongCellValue(row.getCell(columnMap.get("test_case_id"))));
                testGroupingRequestList.add(testGroupingRequest);
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return testGroupingRequestList;
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

    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }
}
