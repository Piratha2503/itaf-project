package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestGroupingResponse {
    private Long id;
    private String name;
    private List<String> testCasesName;
    private String testTypesName;
    private List<String> subModuleName;
    private List<String> mainModuleName;
    private List<String> moduleName;
}
