package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestScenariosRequest {
    private Long id;
    private String name;
    private List<Long> testCasesId;
}
