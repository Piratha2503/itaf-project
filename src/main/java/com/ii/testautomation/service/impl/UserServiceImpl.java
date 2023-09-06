package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.request.UserRequest;
import com.ii.testautomation.entities.Users;
import com.ii.testautomation.enums.LoginStatus;
import com.ii.testautomation.repositories.UserRepository;
import com.ii.testautomation.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;


    @Override
    public void saveUser(UserRequest userRequest) {

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
    public boolean verifyToken(String token)
    {
        try {
            Jwts.parser().setSigningKey("secret").parseClaimsJws(token);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean checkExpiry(String token)
    {
        try {
            Claims claims = Jwts.parser().parseClaimsJws(token).getBody();
            if (claims != null) {

                return claims.getExpiration().before(new Date(System.currentTimeMillis()));
            }
        } catch (ExpiredJwtException ex) {

        }
        return false;
    }

    private String generateToken(Users user)
    {
        Date expiryDate = new Date(System.currentTimeMillis()+60000);
        Claims claims = Jwts.claims()
                .setIssuer(user.getId().toString())
                .setIssuedAt(user.getUpdatedAt())
                .setExpiration(expiryDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,"secret")
                .compact();

        return token;
    }

}
