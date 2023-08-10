package com.ii.testautomation.entities;

import com.ii.testautomation.enums.TestGroupingExecutionStatus;
import com.ii.testautomation.utils.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class TestGrouping extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinColumn(name ="testCases_id", nullable = false)
    private List<TestCases> testCases;
    @ManyToOne
    @JoinColumn(name ="testType_id", nullable = false)
    private TestTypes testType;
    private Boolean executionStatus=TestGroupingExecutionStatus.FALSE.getValue();
}