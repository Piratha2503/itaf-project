package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.repositories.DesignationRepository;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.DesignationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {
    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveDesignation(DesignationRequest designationRequest) {
        Designation designation=new Designation();
        BeanUtils.copyProperties(designationRequest,designation);
        designationRepository.save(designation);
    }

    @Override
    public boolean existsByName(String designationName) {
        return designationRepository.existsByNameIgnoreCase(designationName);
    }

    @Override
    public List<Designation> getAllDesignationByCompanyId(Long CompanyId) {
        List<Users> users = userRepository.findByCompanyUserId(CompanyId);
        List<Designation> designations = new ArrayList<>();
        for (Users user : users) {
            List<Designation> userDesignations = user.getDesignation();
            designations.addAll(userDesignations);
        }
        return designations;
    }
}
