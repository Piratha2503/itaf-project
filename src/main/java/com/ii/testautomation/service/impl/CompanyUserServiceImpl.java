package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.service.CompanyUserService;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Override
    public List<CompanyUserResponse> getAllCompanyUserWithMultiSearch(Pageable pageable, PaginatedContentResponse.Pagination pagination, CompanyUserSearch companyUserSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(companyUserSearch.getCompanyName())) {
            booleanBuilder.and(QCompanyUser.companyUser.companyName.containsIgnoreCase(companyUserSearch.getCompanyName()));
        }
        if (Utils.isNotNullAndEmpty(companyUserSearch.getContactNumber())) {
            booleanBuilder.and(QCompanyUser.companyUser.contactNumber.containsIgnoreCase(companyUserSearch.getContactNumber()));
        }
        if (Utils.isNotNullAndEmpty(companyUserSearch.getEmail())) {
            booleanBuilder.and(QCompanyUser.companyUser.email.containsIgnoreCase(companyUserSearch.getEmail()));
        }
        if (Utils.isNotNullAndEmpty(companyUserSearch.getLastName())) {
            booleanBuilder.and(QCompanyUser.companyUser.lastName.containsIgnoreCase(companyUserSearch.getLastName()));
        }
        if (Utils.isNotNullAndEmpty(companyUserSearch.getFirstName())) {
            booleanBuilder.and(QCompanyUser.companyUser.firstName.containsIgnoreCase(companyUserSearch.getFirstName()));
        }
        if (Utils.isNotNullAndEmpty(companyUserSearch.getLicenseName())) {
            booleanBuilder.and(QCompanyUser.companyUser.licenses.name.containsIgnoreCase(companyUserSearch.getLicenseName()));
        }
        if (Utils.isNotNullAndEmpty(companyUserSearch.getStatus())) {
            booleanBuilder.and(QCompanyUser.companyUser.status.containsIgnoreCase(companyUserSearch.getStatus()));
        }
        if (companyUserSearch.getStartDate() != null) {
            if (Utils.isNotNullAndEmpty(companyUserSearch.getStartDate().toString())) {
                booleanBuilder.and(QCompanyUser.companyUser.startDate.eq(companyUserSearch.getStartDate()));
            }
        }
        if (companyUserSearch.getEndDate() != null) {
            if (Utils.isNotNullAndEmpty(companyUserSearch.getEndDate().toString())) {
                booleanBuilder.and(QCompanyUser.companyUser.endDate.eq(companyUserSearch.getEndDate()));
            }
        }
        if (companyUserSearch.getLicenseDuration() != null) {
            if (Utils.isNotNullAndEmpty(companyUserSearch.getLicenseDuration().toString())) {
                booleanBuilder.and(QCompanyUser.companyUser.licenses.duration.eq(companyUserSearch.getLicenseDuration()));
            }
        }
        if (companyUserSearch.getNoOfUsers() != null) {
            if (Utils.isNotNullAndEmpty(companyUserSearch.getNoOfUsers().toString())) {
                booleanBuilder.and(QCompanyUser.companyUser.licenses.noOfUsers.eq(companyUserSearch.getNoOfUsers()));
            }
        }
        if (companyUserSearch.getNoOfProjects() != null) {
            if (Utils.isNotNullAndEmpty(companyUserSearch.getNoOfProjects().toString())) {
                booleanBuilder.and(QCompanyUser.companyUser.licenses.noOfProjects.eq(companyUserSearch.getNoOfProjects()));
            }
        }
        if (companyUserSearch.getPrice() != null) {
            if ((Utils.isNotNullAndEmpty(companyUserSearch.getPrice().toString()))) {
                booleanBuilder.and(QCompanyUser.companyUser.licenses.price.eq(companyUserSearch.getPrice()));
            }
        }
        List<CompanyUserResponse> companyUserResponseList = new ArrayList<>();
        Page<CompanyUser> companyUserPage = companyUserRepository.findAll(booleanBuilder, pageable);
        List<CompanyUser> companyUserList = companyUserPage.getContent();
        pagination.setPageSize(companyUserPage.getTotalPages());
        pagination.setTotalRecords(companyUserPage.getTotalElements());
        for (CompanyUser companyUser : companyUserList) {
            CompanyUserResponse companyUserResponse = new CompanyUserResponse();
            BeanUtils.copyProperties(companyUser, companyUserResponse);
            companyUserResponse.setNoOfUsers(companyUser.getLicenses().getNoOfUsers());
            companyUserResponse.setLicenseDuration(companyUser.getLicenses().getDuration());
            companyUserResponse.setLicenseName(companyUser.getLicenses().getName());
            companyUserResponse.setLicenseId(companyUser.getLicenses().getId());
            companyUserResponse.setNoOfProjects(companyUser.getLicenses().getNoOfProjects());
            companyUserResponse.setPrice(companyUser.getLicenses().getPrice());
            companyUserResponseList.add(companyUserResponse);
        }
        return companyUserResponseList;
    }

    @Override
    public boolean existsByLicenseId(Long id) {
        return companyUserRepository.existsByLicensesId(id);
    }

    @Override
    public boolean existsById(Long id) {
        return companyUserRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        companyUserRepository.deleteById(id);
    }
}
