package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestGroupingResponse {
    private Long id;
    private String name;
    private String testCasesName;
    private String testTypesName;
    private String subModuleName;
    private String mainModuleName;
    private String moduleName;
}
