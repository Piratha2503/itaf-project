package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.dto.response.UserResponse;

public interface UserService {
    void saveUser(UserRequest userRequest);

    void verifyUser(String token);

    boolean verifyToken(String token);

    boolean checkExpiry(String token);

    boolean existsByEmail(String email);

    boolean existsByDesignationId(Long designationId);
    boolean existsByCompanyUserId(Long id);
    UserResponse getUserById(Long id);
    boolean existsByUsersId(Long usersId);
}
