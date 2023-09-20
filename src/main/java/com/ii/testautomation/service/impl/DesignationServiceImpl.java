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
        Designation designation = new Designation();
        CompanyUser companyUser=new CompanyUser();
        companyUser.setId(designationRequest.getCompanyUserId());
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
        Designation designation =designationRepository.findById(id).get();
        DesignationResponse designationResponse=new DesignationResponse();
        BeanUtils.copyProperties(designation,designationResponse);
        return designationResponse;
    }

    @Override
    public List<DesignationResponse> getAllDesignationByCompanyUserId(Long id) {
        List<DesignationResponse> designationResponseList=new ArrayList<>();
        List<Designation> designationList=designationRepository.findAllDesignationByCompanyUserId(id);
        for(Designation designation:designationList){
            DesignationResponse designationResponse=new DesignationResponse();
           BeanUtils.copyProperties(designation,designationResponse);
           designationResponseList.add(designationResponse);
        }
        return designationResponseList;
    }

    @Override
    public boolean existsByName(String designationName) {
        return designationRepository.existsByNameIgnoreCase(designationName);
    }

    @Override
    public List<DesignationResponse> getAllDesignationByCompanyId(Long companyId) {
        List<Users> usersList = userRepository.findByCompanyUserId(companyId);
        Set<Designation> uniqueDesignations = new HashSet<>();
        for (Users user : usersList) {
            Designation designation = user.getDesignation();
            if (designation != null) {
                uniqueDesignations.add(designation);
            }
        }
        List<DesignationResponse> designationResponseList = new ArrayList<>();
        for (Designation uniqueDesignation : uniqueDesignations) {
            DesignationResponse designationResponse = new DesignationResponse();
            BeanUtils.copyProperties(uniqueDesignation, designationResponse);
            designationResponseList.add(designationResponse);
        }
        return designationResponseList;
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
