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
        CompanyUser companyUser = new CompanyUser();
        Designation designation = new Designation();
        designation.setId(userRequest.getDesignationId());
        companyUser.setId(userRequest.getCompanyUserId());
        user.setDesignation(designation);
        user.setCompanyUser(companyUser);
        BeanUtils.copyProperties(userRequest, user);
        user.setStatus(LoginStatus.NEW.getStatus());
        userRepository.save(user);
        Users userWithId = userRepository.findByEmailIgnoreCase(user.getEmail());
        generateEmail(userWithId);
    }

    @Override
    public boolean existsByUsersId(Long usersId) {
        return userRepository.existsById(usersId);
    }

    @Override
    public void verifyUser(String token) {
       BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Users user = getUserByToken(token);
        user.setStatus(LoginStatus.PENDING.getStatus());
        UUID uuid = UUID.randomUUID();
        String tempPassword = uuid.toString().substring(0,8);
        user.setPassword(bCryptPasswordEncoder.encode(tempPassword));
        userRepository.save(user);
        if (user.getDesignation().getName().equals(Constants.COMPANY_ADMIN.toString()))
        {
            CompanyUser companyAdmin = companyUserRepository.findById(user.getCompanyUser().getId()).get();
            companyAdmin.setStatus(true);
            companyUserRepository.save(companyAdmin);
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(temporaryPasswordSendMailSubject);
        simpleMailMessage.setText(temporaryPasswordSendMailBody+""+tempPassword);
        javaMailSender.send(simpleMailMessage);
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

        if (userRequest.getCompanyUserId() != null) user.setCompanyUser(companyUserRepository.findById(userRequest.getCompanyUserId()).get());

        if (userRequest.getDesignationId() != null) user.setDesignation(designationRepository.findById(userRequest.getDesignationId()).get());

        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUserByCompanyUserId(Pageable pageable, PaginatedContentResponse.Pagination pagination, Long companyUserId, UserSearch userSearch) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

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

    private void generateEmail(Users user) {

        Resource resource = resourceLoader.getResource("classpath:Templates/button.html");
        try {
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();
            String htmlContentAsString = htmlContent.toString();
            String Token = generateExpiringToken(user);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            if (user.getStatus() == LoginStatus.NEW.getStatus()) {
                helper.setSubject(userVerificationMailSubject);
                helper.setText(emailBody.getEmailBody1()+Token+emailBody.getEmailBody2(), true);
            }
            else
            {
                helper.setSubject(passwordResetMailSubject);
                helper.setText(Token, true);
            }
            javaMailSender.send(mimeMessage);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteUserById(Long id) {
        Users users = userRepository.findById(id).get();
        users.setStatus(LoginStatus.DEACTIVATE.getStatus());
        userRepository.save(users);
    }

    @Override
    public String generateNonExpiringToken(String email) {
        Users user = userRepository.findByEmailIgnoreCase(email);
        user.setWrongCount(5);
        userRepository.save(user);
        Claims claims = Jwts.claims().setIssuer(user.getId().toString());
        claims.put("Roll",Constants.COMPANY_ADMIN);
        return Jwts.builder().setClaims(claims).compact();
    }

    @Override
    public String verifyToken(String token) {
        try {
            Users user = getUserByToken(token);
            if (!user.getStatus().equals(LoginStatus.NEW.getStatus())) return statusCodeBundle.getTokenAlreadyUsedMessage();
            else return Constants.TOKEN_VERIFIED;
        } catch (ExpiredJwtException e) {

            return statusCodeBundle.getTokenExpiredMessage();
        }
        catch (Exception e) {
            return statusCodeBundle.getEmailVerificationFailureMessage();
        }
    }

    private Users getUserByToken(String token) {
        Jwts.parser().setSigningKey(Constants.SECRET_KEY.toString()).parseClaimsJws(token);
        Claims claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY.toString()).parseClaimsJws(token).getBody();
        Users user = userRepository.findById(Long.parseLong(claims.getIssuer())).get();
        return user;
    }

    private String generateExpiringToken(Users user) {
        Date expiryDate = new Date(System.currentTimeMillis() + 120000);
        Claims claims = Jwts.claims().setIssuer(user.getId().toString()).setIssuedAt(user.getUpdatedAt()).setExpiration(expiryDate);
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY.toString()).compact();
        return token;
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
            Users user = getUserByToken(token);
            createNewPassword(user,password);
        }

    }
}
