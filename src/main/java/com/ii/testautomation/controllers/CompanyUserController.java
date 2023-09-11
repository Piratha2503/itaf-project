package com.ii.testautomation.controllers;

import com.ii.testautomation.dto.search.CompanyUserSearch;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.service.UserService;
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
    @Autowired
    private UserService userService;

    @DeleteMapping(EndpointURI.COMPANY_USER_BY_ID)
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        if (!companyUserService.existsById(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getFailureCode(),statusCodeBundle.getCompanyUserIdNotExistMessage()));
        if (userService.existsByCompanyUserId(id))
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(), statusCodeBundle.getCompanyUserDeleteDependentCode(),statusCodeBundle.getCompanyUserDeleteDependentMessage()));
        companyUserService.deleteById(id);
        return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),statusCodeBundle.getCommonSuccessCode(),statusCodeBundle.getCompanyUserDeleteSuccessMessage()));
    }

    @GetMapping(EndpointURI.COMPANY_USERS)
    public ResponseEntity<Object> getAllCompanyUsers(@RequestParam(name = "page") int page,
                                                     @RequestParam(name = "size") int size,
                                                     @RequestParam(name = "direction") String direction,
                                                     @RequestParam(name = "sortField") String sortField,
                                                     CompanyUserSearch companyUserSearch) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortField);
        PaginatedContentResponse.Pagination pagination = new PaginatedContentResponse.Pagination(page, size, 0, 0L);
        return ResponseEntity.ok(new PaginatedContentResponse<>(Constants.COMPANY_USERS,companyUserService.getAllCompanyUserWithMultiSearch(pageable,pagination,companyUserSearch),
                RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getAllCompanyUserSuccessfully, pagination));
    }

    @GetMapping(value = EndpointURI.COMPANY_USER_BY_ID)
    public ResponseEntity<Object> GetCompanyUserById(@PathVariable Long id){
        if(!companyUserService.existsById(id)){
            return ResponseEntity.ok(new BaseResponse(RequestStatus.FAILURE.getStatus(),statusCodeBundle.getCompanyUserNotExistCode(),
                    statusCodeBundle.getCompanyUserIdNotExistMessage()));
        }
        return ResponseEntity.ok(new ContentResponse<>(Constants.COMPANY_USERS, companyUserService.getCompanyUserById(id), RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
                statusCodeBundle.getGetCompanyUserByIdSuccessMessage()));
    }
}
