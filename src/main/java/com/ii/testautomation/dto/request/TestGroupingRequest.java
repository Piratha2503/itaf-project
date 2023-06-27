package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestGroupingRequest {
    private Long id;
    private String name;
    private Long testCaseId;
    private Long testTypeId;
}
