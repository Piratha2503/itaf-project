package com.ii.testautomation.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class TestGrouping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name ="testCases_id", nullable = false)
    private TestCases testCases;
    @ManyToOne
    @JoinColumn(name ="testType_id", nullable = false)
    private TestType testType;
}