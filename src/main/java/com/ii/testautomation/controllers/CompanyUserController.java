package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.request.CompanyUserRequest;
import com.ii.testautomation.dto.search.CompanyUserSearch;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CompanyUserController {
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private CompanyUserService companyUserService;



//    @GetMapping(EndpointURI.COMPANY_USERS)
//    public ResponseEntity<Object> getAllCompanyUsers(@RequestParam(name = "page") int page,
//                                                     @RequestParam(name = "size") int size,
//                                                     @RequestParam(name = "direction") String direction,
//                                                     @RequestParam(name = "sortField") String sortField,
//                                                     CompanyUserSearch companyUserSearch) {
//        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
//        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
//        return ResponseEntity.ok(new PaginatedContentResponse<>(Constants.COMPANY_USERS,companyUserService.))
//
//    }

}
