package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.ExecutionHistoryResponse;
import com.ii.testautomation.entities.ExecutionHistory;
import com.ii.testautomation.entities.Project;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.repositories.ExecutionHistoryRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.repositories.TestGroupingRepository;
import com.ii.testautomation.service.ExecutionHistoryService;
import com.ii.testautomation.service.ProjectService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.w3c.dom.Document;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        for (ExecutionHistory executionHistory : executionHistoryList)
        {
            ExecutionHistoryResponse executionHistoryResponse = new ExecutionHistoryResponse();
            executionHistoryResponse.setTestGroupingId(id);
            BeanUtils.copyProperties(executionHistory,executionHistoryResponse);
            executionHistoryResponseList.add(executionHistoryResponse);
        }

        return executionHistoryResponseList;
    }

    @Override
    public String viewReportByExecutionHistoryId(Long id) throws IOException {
        String reportName = executionHistoryRepository.findById(id).get().getReportName();
        Long projectId = testGroupingRepository.findById(id).get().getProject().getId();
        String path = projectRepository.findById(projectId).get().getProjectPath();
        Path reportPath = Path.of(path+File.separator+reportName.toString()+".html");
        String myfile = Files.readString(reportPath);
        return myfile;

    }
    @Override
    public String viewReportWithLastUpdateByExecutionHistoryId(Long id) throws IOException {
        ExecutionHistory latestUpdate = executionHistoryRepository.findFirstByTestGroupingIdOrderByCreatedAtDesc(id);
        Long projectId = testGroupingRepository.findById(id).get().getProject().getId();
        String path = projectRepository.findById(projectId).get().getProjectPath();
        String reportName = latestUpdate.getReportName();
        Path reportpath = Path.of(path+File.separator + reportName.toString() + ".html");
        String reportContent = Files.readString(reportpath);
        return reportContent;
    }



    @Override
    public boolean existByExecutionHistoryId(Long id) {
        return executionHistoryRepository.existsById(id);
    }

    @Override
    public void deleteExecutionHistory(Long id) {
        executionHistoryRepository.deleteById(id);
    }


    @Override
    public boolean existByTestGropingId(Long id) {
        return executionHistoryRepository.existsByTestGroupingId(id);
    }


}
