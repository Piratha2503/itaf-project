package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.UserRequest;

public interface UserService {
    void saveUser(UserRequest userRequest);

    void verifyUser(String token);

    boolean verifyToken(String token);

    boolean checkExpiry(String token);

   // boolean existsByEmail(String email);

 //   boolean existsByStaffIdIgnoreCase(String staffId);

}
