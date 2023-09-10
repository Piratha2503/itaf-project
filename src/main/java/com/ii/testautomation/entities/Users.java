package com.ii.testautomation.entities;

import com.ii.testautomation.utils.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter

public class Users extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String staffName;
    private String status;
    private String staffId;
    private String description;
    private String firstName;
    private String lastName;
    private String contactNumber;
    @ManyToMany
    @JoinColumn(name = "designation_id", nullable = false)
    private List<Designation> designation;
    @ManyToOne
    @JoinColumn(name = "company_user_id", nullable = false)
    private CompanyUser companyUser;
}
