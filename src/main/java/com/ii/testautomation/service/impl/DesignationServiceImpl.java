package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.repositories.DesignationRepository;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.DesignationService;
import org.bouncycastle.jce.provider.JCEMac;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DesignationServiceImpl implements DesignationService {
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyUserRepository companyUserRepository;
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
    public List<Designation> getAllDesignationByCompanyId(Long companyId) {
        List<Users> users = userRepository.findByCompanyUserId(companyId);
        List<Designation> designations = new ArrayList<>();

        for (Users user : users) {
            Designation userDesignation=user.getDesignation();
            if (userDesignation != null) {
                designations.add(userDesignation);
            }
        }
        return designations;
    }

}
