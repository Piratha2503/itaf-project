package com.ii.testautomation.dto.request;

import com.ii.testautomation.entities.TestCases;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestScenarioRequest {
    private Long id;
    private String name;
    private List<Long> testCasesId;
}
