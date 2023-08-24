package com.ii.testautomation.entities;

import com.ii.testautomation.utils.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Scheduling extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean status=true;
    @ManyToMany
    @JoinColumn(name = "test_cases_id",nullable = false)
    private List<TestCases> testCases;
    @ManyToOne
    @JoinColumn(name = "testGrouping_id",nullable = false)
    private TestGrouping testGrouping;
}
