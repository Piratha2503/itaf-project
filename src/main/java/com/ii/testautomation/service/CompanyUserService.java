package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.CompanyUserRequest;

import com.ii.testautomation.dto.response.CompanyUserResponse;
import com.ii.testautomation.dto.search.CompanyUserSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyUserService {
    void saveCompanyUser(CompanyUserRequest companyUserRequest);
    boolean existsByCompanyUserId(Long id);

    boolean isUpdateCompanyUserNameExists(String name, Long id);
   boolean isUpdateEmailExists(String email,Long id);

boolean isUpdateCompanyUserContactNumberExists(String contactNumber,Long id);

    List<CompanyUserResponse> getAllCompanyUserWithMultiSearch(Pageable pageable, PaginatedContentResponse.Pagination pagination, CompanyUserSearch companyUserSearch);

    boolean existsByLicenseId(Long id);
    boolean existsByCompanyId(Long id);
}
