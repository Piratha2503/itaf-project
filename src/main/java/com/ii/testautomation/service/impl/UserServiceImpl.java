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
import com.ii.testautomation.utils.Utils;
import com.querydsl.core.BooleanBuilder;
import com.ii.testautomation.utils.StatusCodeBundle;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        Users userWithId = userRepository.findByEmail(user.getEmail());
        generateEmail(userWithId);
    }

    @Override
    public boolean existsByUsersId(Long usersId) {
        return userRepository.existsById(usersId);
    }

    @Override
    public void verifyUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY.toString()).parseClaimsJws(token).getBody();
        Long id = Long.parseLong(claims.getIssuer());
        Users user = userRepository.findById(id).get();
        user.setStatus(LoginStatus.PENDING.getStatus());
        UUID uuid = UUID.randomUUID();
        String tempPassword = uuid.toString().substring(0, 8);
        user.setPassword(tempPassword);
        userRepository.save(user);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(temporaryPasswordSendMailSubject);
        simpleMailMessage.setText(temporaryPasswordSendMailBody + tempPassword);
        simpleMailMessage.setText(temporaryPasswordSendMailBody+"-->"+tempPassword);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public String verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(Constants.SECRET_KEY.toString()).parseClaimsJws(token);
            Claims claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY.toString()).parseClaimsJws(token).getBody();
            Users user = userRepository.findById(Long.parseLong(claims.getIssuer())).get();
            if (!user.getStatus().equals(LoginStatus.NEW.getStatus())) return statusCodeBundle.getTokenAlreadyUsedMessage();
            else return Constants.TOKEN_VERIFIED;
        } catch (ExpiredJwtException e) {

            return statusCodeBundle.getTokenExpiredMessage();
        }
        catch (Exception e) {
            return statusCodeBundle.getEmailVerificationFailureMessage();
        }
    }

    @Override
    public boolean checkExpiry(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(Constants.SECRET_KEY.toString()).parseClaimsJws(token.toString()).getBody();
            Users users = userRepository.findById(Long.parseLong(claims.getIssuer())).get();

            if (claims != null) {
                Timestamp timestamp = users.getUpdatedAt();
                return claims.getExpiration().before(new Date(timestamp.getTime()+120000));
            }
        } catch (ExpiredJwtException ex) {

        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByContactNo(String contactNo) {
        return userRepository.existsByContactNumberIgnoreCase(contactNo);
    }

    private String generateToken(Users user) {
        Date expiryDate = new Date(System.currentTimeMillis() + 120000);
        Claims claims = Jwts.claims().setIssuer(user.getId().toString()).setIssuedAt(user.getUpdatedAt()).setExpiration(expiryDate);
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY.toString()).compact();
        return token;
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

        if (userRequest.getEmail() == null) newUser.setEmail(user.getEmail());
        else newUser.setEmail(userRequest.getEmail());

        if (userRequest.getContactNumber() == null) newUser.setContactNumber(user.getContactNumber());
        else newUser.setContactNumber(userRequest.getContactNumber());

        if (userRequest.getFirstName() == null) newUser.setFirstName(user.getFirstName());
        else newUser.setFirstName(userRequest.getFirstName());

        if (userRequest.getLastName() == null) newUser.setLastName(user.getLastName());
        else newUser.setLastName(userRequest.getLastName());

        if (userRequest.getCompanyUserId() == null) newUser.setCompanyUser(user.getCompanyUser());
        else newUser.setCompanyUser(companyUserRepository.findById(userRequest.getCompanyUserId()).get());

        if (userRequest.getDesignationId() == null) newUser.setDesignation(user.getDesignation());
        else newUser.setDesignation(designationRepository.findById(userRequest.getDesignationId()).get());

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
            String Token = generateToken(user);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(user.getEmail());
            if (user.getStatus() == LoginStatus.NEW.getStatus()) {
                helper.setSubject(userVerificationMailSubject);
                helper.setText(userVerificationMailBody + emailBody.getEmailBody1() + Token + emailBody.getEmailBody2(), true);
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

}
