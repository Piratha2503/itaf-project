package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.ExecutionHistoryResponse;
import com.ii.testautomation.entities.ExecutionHistory;
import com.ii.testautomation.entities.Project;
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
import java.sql.Timestamp;
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
        ExecutionHistory executionHistory = executionHistoryRepository.findById(id).get();
        String reportName = executionHistory.getReportName();
        Long testGroupingId = executionHistory.getTestGrouping().getId();
        Long projectId = testGroupingRepository.findById(testGroupingId).get().getProject().getId();
        String path = projectRepository.findById(projectId).get().getProjectPath();
        Path reportPath = Path.of(path + File.separator + reportName.toString() + ".html");
        String myfile = Files.readString(reportPath);
        return myfile;

    }

    @Override
    public String viewReportWithLastUpdateByExecutionHistoryId(Long id) throws IOException {
        ExecutionHistory latestUpdate = executionHistoryRepository.findFirstByTestGroupingIdOrderByCreatedAtDesc(id);
        Long projectId = testGroupingRepository.findById(id).get().getProject().getId();
        String path = projectRepository.findById(projectId).get().getProjectPath();
        String reportName = latestUpdate.getReportName();
        Path reportpath = Path.of(path + File.separator + reportName.toString() + ".html");
        String reportContent = Files.readString(reportpath);
        return reportContent;
    }

    @Override
    public boolean deleteExecutionHistory(Long id, Long projectId) {
        Project projectOptional = projectRepository.findById(projectId).get();
        if (projectOptional != null) {
            String historyReport = projectOptional.getProjectPath() + File.separator + executionHistoryRepository.findById(id).get().getReportName().toString() + ".html";
            if (historyReport != null && !historyReport.isEmpty()) {
                if (deleteReport(historyReport)) {
                    executionHistoryRepository.deleteById(id);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean deleteReport(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists() && fileToDelete.isFile()) {
            return fileToDelete.delete();
        }
        return false;
    }

    @Override
    public List<ExecutionHistoryResponse> executionHistoryDateFilter(Long id, Timestamp startDate, Timestamp endDate) {
        List<ExecutionHistoryResponse> executionHistoryResponseList = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = executionHistoryRepository.findByTestGroupingIdAndCreatedAtBetween(id, startDate, endDate);
        for (ExecutionHistory executionHistory : executionHistoryList) {
            ExecutionHistoryResponse executionHistoryResponse = new ExecutionHistoryResponse();
            BeanUtils.copyProperties(executionHistory, executionHistoryResponse);
            executionHistoryResponse.setTestGroupingId(id);
            executionHistoryResponseList.add(executionHistoryResponse);
        }

        return executionHistoryResponseList;
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
