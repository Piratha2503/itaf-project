package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;

public interface DesignationService {
    void saveDesignation(DesignationRequest designationRequest);

    boolean existsByName(String designationName);

    boolean existById(Long id);

    boolean existsByNameIdNot(Long id,String name);

    DesignationResponse GetDesignationById(Long id);
}
