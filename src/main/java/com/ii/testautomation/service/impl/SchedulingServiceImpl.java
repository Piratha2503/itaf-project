package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.SchedulingRequest;
import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.entities.*;
import com.ii.testautomation.repositories.*;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.SchedulingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@PropertySource("classpath:application.properties")
@Service
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired
    private SchedulingRepository schedulingRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Autowired
    private TestScenariosRepository testScenariosRepository;
    @Autowired
    private TestGroupingRepository testGroupingRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ExecutedTestCaseRepository executedTestCaseRepository;

    @Override
    public void saveTestScheduling(SchedulingRequest schedulingRequest) {
        Scheduling scheduling = new Scheduling();
        BeanUtils.copyProperties(schedulingRequest, scheduling);
        TestGrouping testGrouping = testGroupingRepository.findById(schedulingRequest.getGroupId()).get();
        scheduling.setTestGrouping(testGrouping);
        List<TestCases> schedulingTestCasesLists = new ArrayList<>();
        int mapSize = schedulingRequest.getTestScenario().size() + schedulingRequest.getTestCase().size();
        for (int i = 0; i <= mapSize; i++) {
            for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestScenario().entrySet()) {
                if (entry.getKey() == i) {
                    TestScenarios testScenarios = testScenarioRepository.findById(entry.getValue()).get();
                    List<TestCases> testCasesList = testScenarios.getTestCases();
                    for (TestCases testCases : testCasesList
                    ) {
                        schedulingTestCasesLists.add(testCases);
                    }
                }
            }
            for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestCase().entrySet()) {
                if (entry.getKey() == i) {
                    TestCases testCases = testCasesRepository.findById(entry.getValue()).get();
                    schedulingTestCasesLists.add(testCases);
                    break;
                }
            }
        }
        scheduling.setTestCases(schedulingTestCasesLists);
        schedulingRepository.save(scheduling);
    }

    @Scheduled(cron = "${schedule.time.cron}")
    public void autoScheduling() {

        List<Scheduling> schedulingList = schedulingRepository.findAll();
        Long projectId = null;
        Long groupId = null;
        Long schedulingId = null;

        for (Scheduling scheduling : schedulingList
        ) {
            if (scheduling.isStatus()) {
                schedulingId = scheduling.getId();
                groupId = scheduling.getTestGrouping().getId();
                if (scheduling.getTestCases() != null) {
                    for (TestCases testCases : scheduling.getTestCases()) {
                        projectId = testCases.getSubModule().getMainModule().getModules().getProject().getId();
                        try {
                            schedulingExecution(schedulingId, projectId, groupId);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

    }


    public void schedulingExecution(Long schedulingId, Long projectId, Long groupingId) throws IOException {
        Scheduling scheduling = schedulingRepository.findById(schedulingId).get();
        if (scheduling.getTestCases() != null && !scheduling.getTestCases().isEmpty()) {
            for (TestCases testCases : scheduling.getTestCases()
            ) {
                ExecutedTestCase executedTestCase = new ExecutedTestCase();
                executedTestCase.setTestCases(testCases);
                executedTestCaseRepository.save(executedTestCase);
            }
        }
        List<String> excelFiles = testGroupingRepository.findById(groupingId).get().getExcelFilePath();
        String projectPath = projectRepository.findById(projectId).get().getProjectPath();
        if (excelFiles != null) {
            for (String excel : excelFiles) {
                Path excelPath = Path.of(excel);
                try {
                    byte[] excelBytes = Files.readAllBytes(excelPath);
                    String excelFileName = excelPath.getFileName().toString();
                    Path destinationPath = Path.of(projectPath, excelFileName);
                    Files.write(destinationPath, excelBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String savedFilePath = projectRepository.findById(projectId).get().getJarFilePath();
        File jarFile = new File(savedFilePath);
        String jarFileName = jarFile.getName();
        String jarDirectory = jarFile.getParent();
        try {
            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", "-jar", jarFileName);
            runProcessBuilder.directory(new File(jarDirectory));
            runProcessBuilder.redirectErrorStream(true);
            Process runProcess = runProcessBuilder.start();
            runProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination) {
        List<SchedulingResponse> schedulingResponseList = new ArrayList<>();
        Page<Scheduling> schedulingList = schedulingRepository.findByTestGrouping_ProjectId(pageable,projectId);
        pagination.setTotalRecords(schedulingList.getTotalElements());
        pagination.setTotalPages(schedulingList.getTotalPages());
        for (Scheduling scheduling : schedulingList)
        {
            SchedulingResponse schedulingResponse = new SchedulingResponse();
            BeanUtils.copyProperties(scheduling,schedulingResponse);
            schedulingResponseList.add(schedulingResponse);
        }
        return schedulingResponseList;
    }
}