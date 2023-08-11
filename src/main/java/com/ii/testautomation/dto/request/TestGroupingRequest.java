package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestGroupingRequest {
    private Long id;
    private String name;
    private List<Long> testCaseId;
    private Long testTypeId;
    private List<Long> moduleId;
    private List<Long> mainModuleId;
    private List<Long> subModuleId;

}
