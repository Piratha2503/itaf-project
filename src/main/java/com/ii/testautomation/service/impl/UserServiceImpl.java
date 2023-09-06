package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUser(UserRequest userRequest) {

    }
}
