package com.ii.testautomation.service.impl;

import com.ii.testautomation.config.EmailConfiguration;
import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.dto.response.UserResponse;
import com.ii.testautomation.dto.search.UserSearch;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.entities.QUsers;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.enums.LoginStatus;
import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.repositories.DesignationRepository;
import com.ii.testautomation.repositories.ProjectRepository;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.EmailAndTokenService;
import com.ii.testautomation.service.UserService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EmailBody;
import com.ii.testautomation.utils.StatusCodeBundle;
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@PropertySource("classpath:MessagesAndCodes.properties")
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyUserRepository companyUserRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmailConfiguration emailConfiguration;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private EmailBody emailBody;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    private StatusCodeBundle statusCodeBundle;
    @Autowired
    private EmailAndTokenService emailAndTokenService;

    @Value("${user.verification.email.subject}")
    private String userVerificationMailSubject;
    @Value("${user.verification.email.body}")
    private String userVerificationMailBody;
    @Value("${reset.password.email.subject}")
    private String passwordResetMailSubject;
    @Value("${reset.password.email.body}")
    private String passwordResetMailBody;
    @Value("${email.send.temporaryPassword.subject}")
    private String temporaryPasswordSendMailSubject;
    @Value("${email.send.temporaryPassword.body}")
    private String temporaryPasswordSendMailBody;

    @Override
    public void saveUser(UserRequest userRequest) {
        Users user = new Users();
        Designation designation = designationRepository.findById(userRequest.getDesignationId()).get();
        CompanyUser companyUser = companyUserRepository.findById(userRequest.getCompanyUserId()).get();
        user.setDesignation(designation);
        user.setCompanyUser(companyUser);
        BeanUtils.copyProperties(userRequest, user);
        user.setStatus(LoginStatus.NEW.getStatus());
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsersId(Long usersId) {
        return userRepository.existsById(usersId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByContactNo(String contactNo) {
        return userRepository.existsByContactNumberIgnoreCase(contactNo);
    }

    @Override
    public boolean existsByCompanyUserId(Long id) {
        return userRepository.existsByCompanyUserId(id);
    }

    @Override
    public UserResponse getUserById(Long id) {
        Users user = userRepository.findById(id).get();
        UserResponse userResponse = new UserResponse();
        userResponse.setCompanyUserId(user.getCompanyUser().getId());
        userResponse.setCompanyUserName(user.getCompanyUser().getCompanyName());
        userResponse.setDesignationId(user.getDesignation().getId());
        userResponse.setDesignationName(user.getDesignation().getName());
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public void invalidPassword(String email) {
        Users user = userRepository.findByEmailIgnoreCase(email);
        if (user.getWrongCount()>0)
            user.setWrongCount(user.getWrongCount() - 1);
        else user.setStatus(LoginStatus.LOCKED.getStatus());
        userRepository.save(user);
    }

    @Override
    public boolean existsByStatusAndEmail(String status, String email) {
        return userRepository.existsByStatusAndEmailIgnoreCase(status,email);
    }

    @Override
    public Long getAllUserCountByCompanyUserId(Long companyUserId) {

        List<Users> usersList = userRepository.findByCompanyUserId(companyUserId);
        Long count = usersList.stream().count();
        return count;
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Users user = userRepository.findByEmailIgnoreCase(email);
        return bCryptPasswordEncoder.matches(password,user.getPassword());
    }

    @Override
    public boolean existsByDesignationId(Long designationId) {
        return userRepository.existsByDesignationId(designationId);
    }

    @Override
    public boolean existsByUserId(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    @Override
    public boolean existsByContactNumberAndIdNot(String contactNumber, Long id) {
        return userRepository.existsByContactNumberIgnoreCaseAndIdNot(contactNumber, id);
    }

    @Override
    public void updateUser(UserRequest userRequest) {
        Users newUser = new Users();
        Users user = userRepository.findById(userRequest.getId()).get();
        newUser.setId(userRequest.getId());

        if (userRequest.getEmail() != null) user.setEmail(userRequest.getEmail());

        if (userRequest.getContactNumber() != null) user.setContactNumber(userRequest.getContactNumber());

        if (userRequest.getFirstName() != null) user.setFirstName(userRequest.getFirstName());

        if (userRequest.getLastName() != null) user.setLastName(userRequest.getLastName());

        if (userRequest.getCompanyUserId() != null) {
            CompanyUser companyUser = userRepository.findById(userRequest.getCompanyUserId()).get().getCompanyUser();
            user.setCompanyUser(companyUser);
        }

        if (userRequest.getDesignationId() != null) user.setDesignation(designationRepository.findById(userRequest.getDesignationId()).get());

        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUserByCompanyUserId(Pageable pageable, PaginatedContentResponse.Pagination pagination, Long userId, UserSearch userSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Long companyUserId = userRepository.findById(userId).get().getCompanyUser().getId();
        if (Utils.isNotNullAndEmpty(userSearch.getFirstName())) {
            booleanBuilder.and(QUsers.users.firstName.containsIgnoreCase(userSearch.getFirstName()));
        }
        if (Utils.isNotNullAndEmpty(userSearch.getLastName())) {
            booleanBuilder.and(QUsers.users.lastName.containsIgnoreCase(userSearch.getLastName()));
        }
        if (Utils.isNotNullAndEmpty(userSearch.getCompanyUserName())) {
            booleanBuilder.and(QUsers.users.companyUser.companyName.containsIgnoreCase(userSearch.getCompanyUserName()));
        }
        if (Utils.isNotNullAndEmpty(userSearch.getDesignationName())) {
            booleanBuilder.and(QUsers.users.designation.name.containsIgnoreCase(userSearch.getDesignationName()));
        }
        if (companyUserId!=null) {
            booleanBuilder.and(QUsers.users.companyUser.id.eq(companyUserId));
        }
        List<UserResponse> userResponseList = new ArrayList<>();
        Page<Users> usersPage = userRepository.findAll(booleanBuilder,pageable);
        pagination.setTotalRecords(usersPage.getTotalElements());
        pagination.setPageSize(usersPage.getTotalPages());

        for (Users users : usersPage) {
            if (users.getDesignation().getName().equals("ITAF admin")) continue;
            UserResponse userResponse = new UserResponse();
            userResponse.setCompanyUserId(users.getCompanyUser().getId());
            userResponse.setDesignationId(users.getDesignation().getId());
            userResponse.setCompanyUserName(users.getCompanyUser().getCompanyName());
            userResponse.setDesignationName(users.getDesignation().getName());
            BeanUtils.copyProperties(users, userResponse);
            userResponseList.add(userResponse);
        }
        return userResponseList;
    }

    @Override
    public String generateNonExpiringToken(String email) {
        Users user = userRepository.findByEmailIgnoreCase(email);
        return emailAndTokenService.generateToken(user);
    }

    @Override
    public void deleteUserById(Long id) {
        Users users = userRepository.findById(id).get();
        users.setStatus(LoginStatus.DEACTIVATE.getStatus());
        userRepository.save(users);
    }

    public void createNewPassword(Users user, String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setStatus(LoginStatus.ACTIVE.getStatus());
        userRepository.save(user);
    }

    @Override
    public void changePassword(String token, String email, String password) {
        if (token == null) {
            Users user = userRepository.findByEmailIgnoreCase(email);
            createNewPassword(user,password);
        }
        else {
            Users user = emailAndTokenService.getUserByToken(token);
            createNewPassword(user,password);
        }
    }

    @Override
    public void sendMail(String email) {
        Users user = userRepository.findByEmailIgnoreCase(email);
        user.setStatus(LoginStatus.PENDING.getStatus());
        userRepository.save(user);
        emailAndTokenService.sendTokenToEmail(user);
    }
}
