package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
@Setter
@Getter
public class SchedulingRequest {
    private Long id;
    private String name;
    private boolean status;
    private Map<Integer, Long> testScenario;
    private Map<Integer, Long> testCase;
    private Long groupId;
    private Long projectId;
    private LocalDateTime startDateTime;
    private Long Year;
    private Long  hour;
    private Long minutes;
    private Long duration;
    private Long noOfTimes;
    private String month;

}