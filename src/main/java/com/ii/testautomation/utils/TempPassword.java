package com.ii.testautomation.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@PropertySource("classpath:emailConfig.properties")
public class TempPassword {

    @Value("${temp.password.set.random}")
    private String passwordCharacters;
    @Value("${temp.password.length}")
    private int passwordLength;

    public String TempPassword()
    {
        String characters = passwordCharacters;
        int length = passwordLength;
        Random random = new Random();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();
       return randomString;
    }
}
