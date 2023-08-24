package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SchedulingRequest {
    private Long id;
    private String name;
    private boolean status;
    private Map<Integer, Long> testScenario;
    private Map<Integer, Long> testCase;
    private Long groupId;
    private Long projectId;

}