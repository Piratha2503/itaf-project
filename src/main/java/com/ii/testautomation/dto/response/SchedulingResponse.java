package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchedulingResponse {
    private Long id;
    private String name;
    private String testGroupingName;
    private Long testGroupingId;
    private List<Long> testCasesId;
    private  List<String> testCasesName;
    private List<Long> testScenariosId;
    private  List<String> testScenariosName;



}
