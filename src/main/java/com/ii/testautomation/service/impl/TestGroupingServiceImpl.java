package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.SubModulesRequest;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
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
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
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
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
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
            booleanBuilder.and(QTestGrouping.testGrouping.testCases.name.eq(testGroupingSearch.getTestCaseName()));
        }
        if (Utils.isNotNullAndEmpty(testGroupingSearch.getTestTypeName())) {
            booleanBuilder.and(QTestGrouping.testGrouping.testType.name.eq(testGroupingSearch.getTestTypeName()));
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
            testGroupingResponse.setModuleName(testGrouping.getTestCases().getSubModule().getMainModule().getModule().getName());
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
                testGroupingRequest.setTestCaseId(Long.parseLong(csvRecord.get("testCaseId")));
                testGroupingRequest.setTestTypeId(Long.parseLong(csvRecord.get("testTypeId")));
                testGroupingRequestList.add(testGroupingRequest);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return testGroupingRequestList;
    }
    private Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();

        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue().toLowerCase();
            columnMap.put(cellValue, cell.getColumnIndex());
        }

        return columnMap;
    }

    @Override
    public List<TestGroupingRequest> excelToTestGroupingRequest(InputStream inputStream) {
        List<TestGroupingRequest> testGroupingRequestList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip the header row
                }
                TestGroupingRequest testGroupingRequest = new TestGroupingRequest();
                testGroupingRequest.setName(getStringCellValue(row.getCell(columnMap.get("name"))));
                testGroupingRequest.setTestCaseId(getLongCellValue(row.getCell(columnMap.get("testCaseId"))));
                testGroupingRequest.setTestTypeId(getLongCellValue(row.getCell(columnMap.get("testTypeId"))));
                testGroupingRequestList.add(testGroupingRequest);
            }
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
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }


}
