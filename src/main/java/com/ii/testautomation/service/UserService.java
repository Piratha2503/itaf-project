package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.dto.response.MainModulesResponse;
import com.ii.testautomation.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    void saveUser(UserRequest userRequest);

    void verifyUser(String token);

    boolean verifyToken(String token);

    boolean checkExpiry(String token);

    boolean existsByEmail(String email);

    boolean existsByContactNo(String contactNo);

    boolean existsByDesignationId(Long designationId);
    boolean existsByCompanyUserId(Long id);

    List<UserResponse> getUserByCompanyId(Long id);
}
