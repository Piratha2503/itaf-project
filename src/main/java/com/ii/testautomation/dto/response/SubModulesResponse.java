package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubModulesResponse {
    private Long id;
    private String name;
    private String prefix;
    private String mainModuleName;
    private String moduleName;
}