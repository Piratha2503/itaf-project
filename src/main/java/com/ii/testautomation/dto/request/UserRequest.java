package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private Long id;
    private String email;
    private String password;
    private String status;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private Long companyUserId;
    private Long designationId;
}
