package com.ii.testautomation.entities;

import com.ii.testautomation.service.CompanyUser;
import com.ii.testautomation.utils.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Designation extends DateAudit {
    private Long id;
    private String name;
    @ManyToMany
    @JoinColumn(name = " companyUser_id",nullable = false)
    private CompanyUser companyUser;
}
