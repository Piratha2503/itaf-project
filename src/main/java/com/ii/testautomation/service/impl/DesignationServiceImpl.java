package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.DesignationRequest;
import com.ii.testautomation.dto.response.DesignationResponse;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
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
    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Override
    public void saveDesignation(DesignationRequest designationRequest) {
        Designation designation = new Designation();
        CompanyUser companyUser=userRepository.findById(designationRequest.getUserId()).get().getCompanyUser();
        designation.setCompanyUser(companyUser);
        BeanUtils.copyProperties(designationRequest, designation);
        designationRepository.save(designation);
    }

    @Override
    public boolean existsByNameIdNot(Long id, String name) {
        return designationRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public DesignationResponse getDesignationById(Long id) {
        Designation designation = designationRepository.findById(id).get();
        DesignationResponse designationResponse = new DesignationResponse();
        BeanUtils.copyProperties(designation, designationResponse);
        return designationResponse;
    }

    @Override
    public List<DesignationResponse> getAllDesignationByCompanyUserId(Long id) {
        List<DesignationResponse> designationResponseList = new ArrayList<>();
        List<Designation> designationList = designationRepository.findAllDesignationByCompanyUserId(id);
        for (Designation designation : designationList) {
            DesignationResponse designationResponse = new DesignationResponse();
            BeanUtils.copyProperties(designation, designationResponse);
            designationResponseList.add(designationResponse);
        }
        return designationResponseList;
    }

    @Override
    public boolean existsByNameAndCompanyAdminUserId(String designationName, Long userId) {
        Long companyUserId = userRepository.findById(userId).get().getCompanyUser().getId();
        return designationRepository.existsByNameIgnoreCaseAndCompanyUserId(designationName, companyUserId);
    }

    @Override
    public List<DesignationResponse> getAllDesignationByCompanyAdminId(Long userId) {
        List<DesignationResponse> designationResponseList = new ArrayList<>();
        Long companyId = userRepository.findById(userId).get().getCompanyUser().getId();
        List<Designation> designationList = designationRepository.findAllDesignationByCompanyUserId(companyId);
        for (Designation designation : designationList) {
            DesignationResponse designationResponse=new DesignationResponse();
            BeanUtils.copyProperties(designation, designationResponse);
           designationResponseList.add(designationResponse);
        }
        return designationResponseList;
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
