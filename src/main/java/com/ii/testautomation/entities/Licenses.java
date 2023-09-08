package com.ii.testautomation.entities;

import com.ii.testautomation.utils.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Licenses extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long duration;
    private Long no_of_projects;
    private Long no_of_users;
}
