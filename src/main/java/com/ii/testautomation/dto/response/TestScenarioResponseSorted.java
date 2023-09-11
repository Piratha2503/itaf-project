package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestScenarioResponseSorted {
    private Long id;
    private String name;
    private Boolean scheduledStatus;
}
