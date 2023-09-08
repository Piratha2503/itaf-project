package com.ii.testautomation.controllers;

import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CompanyUserController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;
//    public ResponseEntity<Object> saveCompanyUser(@RequestBody CompanyUser companyUser)
//    {
//        if()
//    }
}
