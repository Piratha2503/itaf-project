package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.CompanyUserRequest;

import com.ii.testautomation.dto.response.CompanyUserResponse;
import com.ii.testautomation.dto.search.CompanyUserSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyUserService {

    List<CompanyUserResponse> getAllCompanyUserWithMultiSearch(Pageable pageable, PaginatedContentResponse.Pagination pagination, CompanyUserSearch companyUserSearch);

    boolean existsByLicenseId(Long id);

    boolean isExistCompanyUserName(String companyName);

    boolean isExistByCompanyUserEmail(String email);

    boolean isExistByCompanyUserContactNumber(String contactNumber);

    boolean isExistsByFirstNameAndLastName(String firstName,String lastName);

    void saveCompanyUser(CompanyUserRequest companyUserRequest);

    boolean existsById(Long id);

    void deleteById(Long id);
}
