package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.entities.Designation;

import java.util.List;

public interface DesignationService {
    void saveDesignation(DesignationRequest designationRequest);

    boolean existsByName(String designationName);

    List<DesignationResponse> getAllDesignationByCompanyId(Long companyId);

    boolean existById(Long id);

    boolean existsByNameIdNot(Long id, String name);
}
