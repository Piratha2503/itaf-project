package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.CompanyUserRequest;
import com.ii.testautomation.dto.response.CompanyUserResponse;
import com.ii.testautomation.dto.search.CompanyUserSearch;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Licenses;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyUserService {
  //  List<CompanyUserResponse>getAllCompanyUserWithMultiSearch(Pageable pageable, PaginatedContentResponse.Pagination pagination, CompanyUserSearch companyUserSearch);
}
