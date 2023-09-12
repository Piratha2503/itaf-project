package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String password;
    private String status;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private List<Long> designationId;
    private Long companyUserId;
    private String designationName;
    private String companyUserName;
}
