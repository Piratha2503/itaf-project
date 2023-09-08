package com.ii.testautomation.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CompanyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String status;
    @ManyToOne
    @JoinColumn(name = "license_id",nullable = false)
    private Licenses licenses;

}
