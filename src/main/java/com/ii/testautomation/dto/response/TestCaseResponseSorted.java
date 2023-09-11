package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestCaseResponseSorted {
    private Long id;
    private String name;
    private Boolean scheduledStatus;
}
