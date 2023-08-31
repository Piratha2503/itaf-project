package com.ii.testautomation.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class ProgressBar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Project project;
    @ManyToOne
    private TestGrouping testGrouping;
    private Long totalNoOfTestCases;
    private Long executedTestCaseCount;

}
