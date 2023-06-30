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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.*;
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
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override

    public void saveProject(ProjectRequest projectRequest)
    {
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        projectRepository.save(project);
    }

    @Override
    public void saveProjectList(List<ProjectRequest> projectRequestList) {
        for (ProjectRequest projectRequest:projectRequestList
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
            booleanBuilder.and(QProject.project.name.eq(projectSearch.getName()));
        }
        if (Utils.isNotNullAndEmpty(projectSearch.getCode())) {
            booleanBuilder.and(QProject.project.code.eq(projectSearch.getCode()));
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
    public boolean hasCsvFormat(MultipartFile multipartFile) {
        String expectedContentType = "text/csv";
        String actualContentType = multipartFile.getContentType();
        return expectedContentType.equals(actualContentType);
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

    public List<ProjectRequest> excelToProjectRequest(InputStream inputStream) {
        List<ProjectRequest> projectRequestList = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();
            boolean firstLine = true;

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnMap = getColumnMap(headerRow);

            for (Row row : sheet) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                ProjectRequest projectRequest = new ProjectRequest();

                Cell codeCell = row.getCell(columnMap.get("Code"));
                Cell descriptionCell = row.getCell(columnMap.get("Description"));
                Cell nameCell = row.getCell(columnMap.get("Name"));

                projectRequest.setCode(dataFormatter.formatCellValue(codeCell));
                projectRequest.setDescription(dataFormatter.formatCellValue(descriptionCell));
                projectRequest.setName(dataFormatter.formatCellValue(nameCell));

                projectRequestList.add(projectRequest);
            }

            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return projectRequestList;
    }

    private Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> columnMap = new HashMap<>();

        for (Cell cell : headerRow) {
            String cellValue = cell.getStringCellValue();
            columnMap.put(cellValue, cell.getColumnIndex());
        }

        return columnMap;
    }
}
