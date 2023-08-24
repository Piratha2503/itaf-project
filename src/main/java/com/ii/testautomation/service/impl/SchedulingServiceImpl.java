package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.dto.response.TestCaseResponse;
import com.ii.testautomation.entities.Scheduling;
import com.ii.testautomation.entities.TestCases;
import com.ii.testautomation.entities.TestGrouping;
import com.ii.testautomation.entities.TestScenarios;
import com.ii.testautomation.repositories.SchedulingRepository;
import com.ii.testautomation.service.SchedulingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingServiceImpl implements SchedulingService {


    @Autowired
    SchedulingRepository schedulingRepository;

    @Override
    public boolean existsById(Long id) {
        return schedulingRepository.existsById(id) ;
    }

    @Override
    public SchedulingResponse getSchedulingById(Long id) throws NullPointerException {

        SchedulingResponse schedulingResponse = new SchedulingResponse();
        Scheduling scheduling = schedulingRepository.findById(id).get();
        schedulingResponse.setTestGroupingId(scheduling.getTestGrouping().getId());
        schedulingResponse.setTestGroupingName(scheduling.getTestGrouping().getName());
        List<String> testScenarioNames = new ArrayList<>();
        List<Long> testScenarioIds = new ArrayList<>();
        List<String> testCaseNames = new ArrayList<>();
        List<Long> testCaseIds = new ArrayList<>();


        for (TestScenarios testScenario : scheduling.getTestScenarios()) {
            testScenarioNames.add(testScenario.getName());
            testScenarioIds.add(testScenario.getId());

            for (TestCases testCase : testScenario.getTestCases()) {
                testCaseNames.add(testCase.getName());
                testCaseIds.add(testCase.getId());

            }
        }

        schedulingResponse.setTestScenariosName(testScenarioNames);
        schedulingResponse.setTestScenariosId(testScenarioIds);
        schedulingResponse.setTestCasesName(testCaseNames);
        schedulingResponse.setTestCasesId(testCaseIds);
        BeanUtils.copyProperties(scheduling, schedulingResponse);
        return schedulingResponse;
    }


}






