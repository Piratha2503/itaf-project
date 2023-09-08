package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.LicenseResponse;

import com.ii.testautomation.dto.search.LicensesSearch;
import com.ii.testautomation.entities.Licenses;

import com.ii.testautomation.entities.QLicenses;

import com.ii.testautomation.repositories.LicenseRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.LicenseService;
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
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    private LicenseRepository licenseRepository;

    @Override
    public List<LicenseResponse> multiSearchLicensesWithPagination(Pageable pageable, PaginatedContentResponse.Pagination pagination, LicensesSearch licensesSearch) {
        BooleanBuilder booleanBuilder=new BooleanBuilder();
        if (Utils.isNotNullAndEmpty(licensesSearch.getName())) {
            booleanBuilder.and(QLicenses.licenses.name.containsIgnoreCase(licensesSearch.getName()));
        }
        List<LicenseResponse> licenseResponseList = new ArrayList<>();
        Page<Licenses> licensesPage = licenseRepository.findAll(booleanBuilder,pageable);
        pagination.setPageSize(licensesPage.getTotalPages());
        pagination.setTotalRecords(licensesPage.getTotalElements());

        for (Licenses licenses : licensesPage) {
            LicenseResponse licenseResponse = new LicenseResponse();
            BeanUtils.copyProperties(licenses, licenseResponse);
            licenseResponseList.add(licenseResponse);
        }
        return licenseResponseList;
    }

}