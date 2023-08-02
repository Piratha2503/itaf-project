package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestCaseResponse {
    private Long id;
    private String name;
    private String description;
    private String subModuleName;
    private String moduleName;
    private String mainModuleName;
}
