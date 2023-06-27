package com.ii.testautomation.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MainModules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String prefix;
    @ManyToOne
    @JoinColumn(name = "module_id",nullable = false)
    private Modules modules;
}