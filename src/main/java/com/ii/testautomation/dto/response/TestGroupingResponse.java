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
    private String testTypesName;

    private List<String> subModulesName;
    private List<String> mainModulesName;
    private List<String> modulesName;


}
