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

import javax.transaction.Transactional;
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
        List<Long> testCasesId = new ArrayList<>();
        List<TestScenarios> testScenariosList = new ArrayList<>();
        List<TestCases> testCasessList = new ArrayList<>();
        int mapSize = schedulingRequest.getTestScenario().size() + schedulingRequest.getTestCase().size();
        for (int i = 0; i <= mapSize; i++) {
            for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestScenario().entrySet()) {
                if (entry.getKey() == i) {
                    TestScenarios testScenarios = testScenariosRepository.findById(entry.getValue()).get();
                    testScenariosList.add(testScenarios);
                    List<TestCases> testCasesList = testScenarios.getTestCases();
                    for (TestCases testCases : testCasesList
                    ) {
                        testCasesId.add(testCases.getId());
                    }
                }
            }
            for (Map.Entry<Integer, Long> entry : schedulingRequest.getTestCase().entrySet()) {
                if (entry.getKey() == i) {
                    TestCases testCases = testCasesRepository.findById(entry.getValue()).get();
                    testCasessList.add(testCases);
                    testCasesId.add(testCases.getId());
                    break;
                }
            }
        }
        scheduling.setTestCasesIds(testCasesId);
        scheduling.setTestCases(testCasessList);
        scheduling.setTestScenarios(testScenariosList);
        schedulingRepository.save(scheduling);
    }

    @Transactional
    @Scheduled(cron = "${schedule.time.cron}")
    public void autoScheduling() throws IOException {
        List<Scheduling> schedulingList = schedulingRepository.findAll();
        Long projectId = null;
        Long groupId = null;
        if (schedulingList != null && !schedulingList.isEmpty()) {
            for (Scheduling scheduling : schedulingList
            ) {
                if (scheduling.isStatus()) {
                    groupId = scheduling.getTestGrouping().getId();
                    if (scheduling.getTestCasesIds() != null && !scheduling.getTestCasesIds().isEmpty()) {
                        for (Long testCaseId : scheduling.getTestCasesIds()) {
                            projectId = testCasesRepository.findById(testCaseId).get().getSubModule().getMainModule().getModules().getProject().getId();
                            break;
                        }
                    }
                }
                schedulingExecution(scheduling.getTestCasesIds(), projectId, groupId);
            }
        }
    }


    public void schedulingExecution(List<Long> testCaseIds, Long projectId, Long groupingId) throws IOException {
        for (Long testCaseId : testCaseIds
        ) {
            TestCases testCases = testCasesRepository.findById(testCaseId).get();
            ExecutedTestCase executedTestCase = new ExecutedTestCase();
            executedTestCase.setTestCases(testCases);
            executedTestCaseRepository.save(executedTestCase);
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
        jarExecution(projectId);
    }

    private void jarExecution(Long projectId) {
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
    public boolean existById(Long id) {
        return schedulingRepository.existsById(id);
    }

    @Override
    public void deleteScheduling(Long schedulingId) {
        schedulingRepository.deleteById(schedulingId);
    }

    @Override
    public List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination) {
        List<SchedulingResponse> schedulingResponseList = new ArrayList<>();
        Page<Scheduling> schedulingList = schedulingRepository.findByTestGrouping_ProjectId(pageable, projectId);
        pagination.setTotalRecords(schedulingList.getTotalElements());
        pagination.setTotalPages(schedulingList.getTotalPages());
        for (Scheduling scheduling : schedulingList) {
            SchedulingResponse schedulingResponse = new SchedulingResponse();
            BeanUtils.copyProperties(scheduling, schedulingResponse);
            schedulingResponseList.add(schedulingResponse);
        }
        return schedulingResponseList;
    }

    @Override
    public boolean existsBySchedulingNameByTestGroupingAndProjectId(String name, Long projectId) {
        return schedulingRepository.existsByNameIgnoreCaseAndTestGrouping_TestCases_SubModule_MainModule_Modules_Project_Id(name, projectId);
    }
}
