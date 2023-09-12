package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String password;
    private String status;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private Long designation_id;
    private String designationName;
    private Long company_user_id;
    private String companyUserName;
}
