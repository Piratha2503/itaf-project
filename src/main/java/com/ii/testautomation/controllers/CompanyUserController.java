package com.ii.testautomation.controllers;

import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CompanyUserController {
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private StatusCodeBundle statusCodeBundle;

}
