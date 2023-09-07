package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.UserRequest;

public interface UserService {
    void saveUser(UserRequest userRequest);

    boolean existsByEmail(String email);
    public boolean existsByStaffIdIgnoreCase(String staffId);

}
