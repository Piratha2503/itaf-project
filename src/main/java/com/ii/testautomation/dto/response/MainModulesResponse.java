package com.ii.testautomation.dto.response;

import com.ii.testautomation.entities.Modules;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainModulesResponse
{
    private Long proid;
    private String name;
    private String prefix;
    private Module modules;
}
