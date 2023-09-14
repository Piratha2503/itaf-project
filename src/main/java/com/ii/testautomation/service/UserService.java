package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.dto.response.UserResponse;

public interface UserService {
    void saveUser(UserRequest userRequest);

    void verifyUser(String token);

    String verifyToken(String token);

    boolean existsByEmail(String email);

    boolean existsByUsersId(Long usersId);

    boolean existsByContactNo(String contactNo);

    boolean existsByDesignationId(Long designationId);

    boolean existsByCompanyUserId(Long id);

    boolean existsByUserId(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByContactNumberAndIdNot(String contactNumber, Long id);

    void updateUser(UserRequest userRequest);

    void deleteUserById(Long id);

    UserResponse getUserById(Long id);

    void invalidPassword(String email);

    boolean existsByStatus(String status);

    boolean existsByEmailAndPassword(String email, String password);

    void changePassword(Long id, String password);
}
