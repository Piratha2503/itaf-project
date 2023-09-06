package com.ii.testautomation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private Long id;
    private String email;
    private String password;

}
