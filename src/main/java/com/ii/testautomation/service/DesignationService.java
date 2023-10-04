package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;

import java.util.List;

public interface DesignationService {
    void saveDesignation(DesignationRequest designationRequest);

    boolean existsByNameAndCompanyUserId(String designationName, Long companyUserId);

    List<DesignationResponse> getAllDesignationByCompanyAdminId(Long userId);

    boolean existsById(Long id);

    void deleteDesignationById(Long id);

    boolean existById(Long id);

    boolean existsByNameIdNot(Long id, String name);

    DesignationResponse getDesignationById(Long id);

    List<DesignationResponse> getAllDesignationByCompanyUserId(Long companyUserId);
}
