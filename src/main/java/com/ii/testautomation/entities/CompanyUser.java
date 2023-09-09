package com.ii.testautomation.entities;

import com.ii.testautomation.utils.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
public class CompanyUser extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String status;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "license_id",nullable = false)
    private Licenses licenses;

}
