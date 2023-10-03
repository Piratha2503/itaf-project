package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.dto.search.UserSearch;
import com.ii.testautomation.response.common.PaginatedContentResponse;

import java.awt.print.Pageable;
import java.util.List;

public interface DesignationService {
    void saveDesignation(DesignationRequest designationRequest);

    boolean existsByName(String designationName);

    List<DesignationResponse> getAllDesignationByCompanyId(Long userId);

    boolean existsById(Long id);

    void deleteDesignationById(Long id);

    boolean existById(Long id);

    boolean existsByNameIdNot(Long id, String name);

    DesignationResponse getDesignationById(Long id);

    List<DesignationResponse> getAllDesignationByCompanyUserId(Long companyUserId);
}
