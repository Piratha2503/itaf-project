package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.repositories.DesignationRepository;
import com.ii.testautomation.service.DesignationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignationServiceImpl implements DesignationService {
    @Autowired
    private DesignationRepository designationRepository;

    @Override
    public void saveDesignation(DesignationRequest designationRequest) {
        Designation designation = new Designation();
        BeanUtils.copyProperties(designationRequest, designation);
        designationRepository.save(designation);
    }

    @Override
    public boolean existsByNameIdNot(Long id,String name) {
        return designationRepository.existsByNameIgnoreCaseAndIdNot(name,id);
    }

    @Override
    public DesignationResponse GetDesignationById(Long id) {
        Designation designation =designationRepository.findById(id).get();
        DesignationResponse designationResponse=new DesignationResponse();
        BeanUtils.copyProperties(designation,designationResponse);
        return designationResponse;
    }

    @Override
    public boolean existsByName(String designationName) {
        return designationRepository.existsByNameIgnoreCase(designationName);
    }

    @Override
    public boolean existsById(Long id) {
        return designationRepository.existsById(id);
    }

    @Override
    public void deleteDesignationById(Long id) {
        designationRepository.deleteById(id);
    }

    @Override
    public boolean existById(Long id) {
        return designationRepository.existsById(id);
    }
}
