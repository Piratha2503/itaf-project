package com.ii.testautomation.dto.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserSearch {
    private String firstName;
    private String lastName;
    private String companyUserName;
    private Long companyUserId;
    private String designationName;
    private Long designationId;
}
