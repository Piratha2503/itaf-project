package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUser(UserRequest userRequest) {
        Users user = new Users();
       // user.setStatus(new);
        BeanUtils.copyProperties(userRequest, user);
        userRepository.save(user);

    }
    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    @Override
    public boolean existsByStaffIdIgnoreCase(String staffId) {
        return userRepository.existsByEmailIgnoreCase(staffId);
    }

}
