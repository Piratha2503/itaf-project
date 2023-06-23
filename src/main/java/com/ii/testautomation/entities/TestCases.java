package com.ii.testautomation.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class TestCases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "submodule_id",nullable = false)
    private SubModule subModule;


}