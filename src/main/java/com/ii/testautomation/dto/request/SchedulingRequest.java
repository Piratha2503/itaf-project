package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class SchedulingRequest {
    private Long id;
    private String name;
    private String status;
    private Long groupId;
    Map<Integer,Long> testCase;
    Map<Integer,Long> testScenario;
}
