package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchedulingResponse {
    private Long id;
    private String name;
    private Long testGroupingId;
    private String testGroupingName;
    private List<Long> testCasesIds;
    private List<String> testCasesNames;
    private List<Long> testScenarioIds;
    private List<String> testScenarioNames;

}
