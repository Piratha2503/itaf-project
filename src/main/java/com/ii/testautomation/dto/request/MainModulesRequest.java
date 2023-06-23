package com.ii.testautomation.dto.request;

import com.ii.testautomation.entities.Modules;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainModulesRequest
{
    private Long modid;
    private String name;
    private String prefix;
   // private Module modules;
}
