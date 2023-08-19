package com.ii.testautomation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String code;
    private byte[] jarFile;
    private byte[] configFile;

}
