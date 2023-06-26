package com.ii.testautomation.dto.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainModulesResponse
{
    private Long id;
    private String name;
    private String prefix;
    private Module modules;
}
