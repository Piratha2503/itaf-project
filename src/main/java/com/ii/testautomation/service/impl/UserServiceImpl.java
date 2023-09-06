package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.entities.User;
import com.ii.testautomation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserService userService;

    @Override
    public void saveUser(UserRequest userRequest) {
        User user=new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
   
    }
}
