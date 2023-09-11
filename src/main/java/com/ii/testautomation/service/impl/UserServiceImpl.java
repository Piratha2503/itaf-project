package com.ii.testautomation.service.impl;

import com.ii.testautomation.config.EmailConfiguration;
import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.enums.LoginStatus;
import com.ii.testautomation.repositories.CompanyUserRepository;
import com.ii.testautomation.repositories.DesignationRepository;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Value("${user.verification.email.subject}")
    private String userVerificationMailSubject;
    @Value("${user.verification.email.body}")
    private String userVerificationMailBody;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void verifyUser(String token) {
        Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
        Long id = Long.parseLong(claims.getIssuer());
        Users user = userRepository.findById(id).get();
        user.setStatus(LoginStatus.VERIFIED.getStatus());
        userRepository.save(user);
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey("secret").parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkExpiry(String token) {
        try {
            Claims claims = Jwts.parser().parseClaimsJws(token).getBody();
            if (claims != null) {

                return claims.getExpiration().before(new Date(System.currentTimeMillis()));
            }
        } catch (ExpiredJwtException ex) {

        }
        return false;
    }

    private String generateToken(Users user) {
        Date expiryDate = new Date(System.currentTimeMillis() + 60000);
        Claims claims = Jwts.claims()
                .setIssuer(user.getId().toString())
                .setIssuedAt(user.getUpdatedAt())
                .setExpiration(expiryDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();

        return token;
    }

    @Override
    public boolean existsByCompanyUserId(Long id) {
        return userRepository.existsByCompanyUserId(id);
    }

    @Override
    public boolean existsByUserId(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email,id);
    }

    @Override
    public boolean existsByContactNumberAndIdNot(String contactNumber, Long id) {
        return userRepository.existsByContactNumberAndIdNot(contactNumber, id);
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


}
