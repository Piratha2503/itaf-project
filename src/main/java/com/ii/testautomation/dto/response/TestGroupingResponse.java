package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestGroupingResponse {
    private Long id;
    private String name;
    private List<String> testCaseName;
    private Long testTypeId;
    private String testTypeName;
    private List<Long> testCaseIds;
    private List<Long> testScenarioIds;
    private List<String> testCaseNames;
    private List<String> testScenarioName;
    private List<String> subModuleName;
    private List<String> mainModuleName;
    private List<String> moduleName;
    private List<String> testScenariosName;

}
