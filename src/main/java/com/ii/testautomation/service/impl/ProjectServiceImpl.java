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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    public List<ProjectRequest> importProjectFile(MultipartFile multipartFile) {
        List<ProjectRequest> projectRequestList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
            String line;
            boolean firstLine = true;
            String header[] = null;
            while ((line = bufferedReader.readLine()) != null) {
                String data[] = line.split(",");
                if (firstLine) {
                    header = data;
                    firstLine = false;
                    continue;
                }
                ProjectRequest projectRequest = new ProjectRequest();
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals("code")) {
                        projectRequest.setCode(data[i]);
                    }
                    if (header[i].equals("name")) {
                        projectRequest.setName(data[i]);
                    }
                    if (header[i].equals("description")) {
                        projectRequest.setDescription(data[i]);
                    }
                }
                projectRequestList.add(projectRequest);
            }
        } catch (Exception e) {
            System.out.println(e + "not save");
        }
        return projectRequestList;
    }

    @Override
    public List<ProjectRequest> importProjectFileXls(MultipartFile multipartFile) {
        String header[] = null;
        List<ProjectRequest> projectRequestList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                ProjectRequest projectRequest = new ProjectRequest();
                if (row.getRowNum() == 0) {
                    for (int i = 0; i < 3; i++) {
                        header[i] = row.getCell(i).getStringCellValue();
                    }
                    continue;
                }
                for (int i = 0; i < 3; i++) {
                    if (header[i].equals("code")) {
                        if (row.getCell(i).getStringCellValue().isEmpty()) continue;
                        projectRequest.setCode(row.getCell(i).getStringCellValue());
                    }
                    if (header[i].equals("name")) {
                        if (row.getCell(i).getStringCellValue().isEmpty()) continue;
                        projectRequest.setName(row.getCell(i).getStringCellValue());
                    }
                    if (header[i].equals("description")) {
                        if (row.getCell(i).getStringCellValue().isEmpty()) continue;
                        projectRequest.setDescription(row.getCell(i).getStringCellValue());
                    }
                }
                projectRequestList.add(projectRequest);
            }

        } catch (Exception e) {
            System.out.println("error");
        }
        return projectRequestList;
    }
}
