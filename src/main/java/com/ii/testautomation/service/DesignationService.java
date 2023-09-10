package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.DesignationRequest;

public interface DesignationService {
    void saveDesignation(DesignationRequest designationRequest);

    boolean existsByName(String designationName);

    boolean existById(Long id);
}
