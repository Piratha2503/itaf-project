package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.entities.Designation;

import java.util.List;

public interface DesignationService {
    void saveDesignation(DesignationRequest designationRequest);

    boolean existsByName(String designationName);

    //List<Designation> getAllDesignationByCompanyId(Long CompanyId);
}
