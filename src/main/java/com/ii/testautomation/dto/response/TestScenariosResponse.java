package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TestScenariosResponse {

   private Long id;
   private String name;
   private List<String> testCasesName;
   private List<Long> testCaseId;
}
