package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModulesResponse {
    private Long id;
    private String name;
    private String prefix;
    private Long projectId;
    private String projectName;
}
