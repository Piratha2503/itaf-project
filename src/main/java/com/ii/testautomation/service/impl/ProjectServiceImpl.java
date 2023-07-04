package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.ProjectRequest;
import com.ii.testautomation.dto.response.ProjectResponse;
import com.ii.testautomation.dto.search.ProjectSearch;
import com.ii.testautomation.entities.Project;
import com.ii.testautomation.entities.QProject;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.ProjectService;
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

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void saveProject(ProjectRequest projectRequest) {
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        projectRepository.save(project);
    }

    @Override
    public void saveProjectList(List<ProjectRequest> projectRequestList) {
        for (ProjectRequest projectRequest : projectRequestList
        ) {

            Project project = new Project();
            BeanUtils.copyProperties(projectRequest, project);
            projectRepository.save(project);
        }
    }

    @Override
    public boolean existByProjectName(String projectName) {
        return projectRepository.existsByNameIgnoreCase(projectName);
    }

    @Override
    public boolean existByProjectCode(String projectCode) {
        return projectRepository.existsByCodeIgnoreCase(projectCode);
    }

    @Override
    public boolean isUpdateProjectNameExist(String projectName, Long projectId) {
        return projectRepository.existsByNameIgnoreCaseAndIdNot(projectName, projectId);
    }

    @Override
    public boolean isUpdateProjectCodeExist(String projectCode, Long projectId) {
        return projectRepository.existsByCodeIgnoreCaseAndIdNot(projectCode, projectId);
    }

    @Override
    public boolean existByProjectId(Long projectId) {
        return projectRepository.existsById(projectId);
    }

    @Override
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(project, projectResponse);
        return projectResponse;
    }

    @Override
    public List<ProjectResponse> multiSearchProject(Pageable pageable, PaginatedContentResponse.Pagination pagination, ProjectSearch projectSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(projectSearch.getName())) {
            booleanBuilder.and(QProject.project.name.containsIgnoreCase(projectSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(projectSearch.getCode())) {
            booleanBuilder.and(QProject.project.code.containsIgnoreCase(projectSearch.getCode()));
        }
        List<ProjectResponse> projectResponseList = new ArrayList<>();
        Page<Project> projectPage = projectRepository.findAll(booleanBuilder, pageable);

        pagination.setTotalRecords(projectPage.getTotalElements());
        pagination.setPageSize(projectPage.getTotalPages());
        for (Project project : projectPage) {
            ProjectResponse projectResponse = new ProjectResponse();
            BeanUtils.copyProperties(project, projectResponse);
            projectResponseList.add(projectResponse);
        }
        return projectResponseList;
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public List<ProjectRequest> csvToProjectRequest(InputStream inputStream) {
        List<ProjectRequest> projectRequestList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ProjectRequest projectRequest = new ProjectRequest();
                projectRequest.setCode(csvRecord.get("Code"));
                projectRequest.setDescription(csvRecord.get("description"));
                projectRequest.setName(csvRecord.get("name"));
                projectRequestList.add(projectRequest);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return projectRequestList;
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
    public List<ProjectRequest> excelToProjectRequest(MultipartFile multipartFile) {
        List<ProjectRequest> projectRequestList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                ProjectRequest projectRequest = new ProjectRequest();


                Cell codeCell = row.getCell(columnMap.get("code"));
                Cell descriptionCell = row.getCell(columnMap.get("description"));
                Cell nameCell = row.getCell(columnMap.get("name"));

                projectRequest.setCode(dataFormatter.formatCellValue(codeCell));
                projectRequest.setDescription(dataFormatter.formatCellValue(descriptionCell));
                projectRequest.setName(dataFormatter.formatCellValue(nameCell));

//                projectRequest.setCode(getStringCellValue(row.getCell(0)));
//                projectRequest.setDescription(getStringCellValue(row.getCell(1)));
//                projectRequest.setName(getStringCellValue(row.getCell(2)));

                projectRequestList.add(projectRequest);
            }

//            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return projectRequestList;
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



    public File convertXlsxToCsv(MultipartFile xlsxFile) throws IOException {
        File tempCsvFile = File.createTempFile("temp", ".csv");

        File parentDir = new File("D:/Temp");
        tempCsvFile = new File(parentDir, tempCsvFile.getName());

        Workbook workbook = new XSSFWorkbook(xlsxFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempCsvFile))) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    CellType cellType = cell.getCellType();
                    String cellValue = "";

                    if (cellType == CellType.STRING) {
                        cellValue = cell.getStringCellValue();
                    } else if (cellType == CellType.NUMERIC) {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    } else if (cellType == CellType.BOOLEAN) {
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                    }

                    writer.append(cellValue);
                    writer.append(",");
                }
                writer.newLine();
            }
        }

        return tempCsvFile;
    }

    @Override
    public void addToErrorMessages(Map<String, List<Integer>> errorMessages, String key, int value) {
        List<Integer> errorList = errorMessages.getOrDefault(key, new ArrayList<>());
        errorList.add(value);
        errorMessages.put(key, errorList);
    }
}
