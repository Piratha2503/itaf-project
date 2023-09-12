package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.UserRequest;

public interface UserService {
    void saveUser(UserRequest userRequest);

    void verifyUser(String token);

    boolean verifyToken(String token);

    boolean checkExpiry(String token);

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
}
