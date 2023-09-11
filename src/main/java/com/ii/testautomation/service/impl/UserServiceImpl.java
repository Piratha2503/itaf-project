package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.enums.LoginStatus;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.repositories.DesignationRepository;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
   private UserRepository userRepository;
    @Autowired
   private DesignationRepository designationRepository;

    @Override
    public void saveUser(UserRequest userRequest) {
        Users user = new Users();
        CompanyUser companyUser=new CompanyUser();
        companyUser.setId(userRequest.getCompanyUserId());
        List<Designation> designationList=new ArrayList<>();
        user.setStatus(LoginStatus.NEW.getStatus());
        BeanUtils.copyProperties(userRequest, user);
        userRepository.save(user);
        generateToken(user);
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

}
