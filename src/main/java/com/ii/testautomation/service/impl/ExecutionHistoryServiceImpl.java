package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.ExecutionHistoryResponse;
import com.ii.testautomation.entities.ExecutionHistory;
import com.ii.testautomation.repositories.ExecutionHistoryRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.repositories.TestGroupingRepository;
import com.ii.testautomation.service.ExecutionHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExecutionHistoryServiceImpl implements ExecutionHistoryService {
    @Autowired
    private ExecutionHistoryRepository executionHistoryRepository;
    @Autowired
    private TestGroupingRepository testGroupingRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Value("${reports.file.save.path}")
    private String fileFolder;

    @Override
    public List<ExecutionHistoryResponse> viewByTestGroupingId(Long id) {
        List<ExecutionHistoryResponse> executionHistoryResponseList = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = executionHistoryRepository.findAllByTestGroupingIdOrderByCreatedAtDesc(id);
        for (ExecutionHistory executionHistory : executionHistoryList) {
            ExecutionHistoryResponse executionHistoryResponse = new ExecutionHistoryResponse();
            executionHistoryResponse.setTestGroupingId(id);
            BeanUtils.copyProperties(executionHistory, executionHistoryResponse);
            executionHistoryResponseList.add(executionHistoryResponse);
        }

        return executionHistoryResponseList;
    }

    @Override
    public String viewReportByExecutionHistoryId(Long id) throws IOException {
        String reportName = executionHistoryRepository.findById(id).get().getReportName();
        Long projectId = testGroupingRepository.findById(id).get().getProject().getId();
        String path = projectRepository.findById(projectId).get().getProjectPath();
        Path reportPath = Path.of(path + File.separator + reportName.toString() + ".html");
        String myfile = Files.readString(reportPath);
        return myfile;

    }

    @Override
    public String viewReportWithLastUpdateByExecutionHistoryId(Long id) throws IOException {
        ExecutionHistory latestUpdate = executionHistoryRepository.findFirstByTestGroupingIdOrderByCreatedAtDesc(id);
        String reportName = latestUpdate.getReportName();
        Path path = Path.of(fileFolder + reportName.toString() + ".html");
        String reportContent = Files.readString(path);
        return reportContent;
    }

    @Override
    public void deleteExecutionHistory(Long id) {
        executionHistoryRepository.deleteById(id);
    }

    @Override
    public boolean existByTestGropingId(Long id) {
        return executionHistoryRepository.existsByTestGroupingId(id);
    }

    @Override
    public boolean existByExecutionHistoryId(Long id) {
        return executionHistoryRepository.existsById(id);
    }

}
