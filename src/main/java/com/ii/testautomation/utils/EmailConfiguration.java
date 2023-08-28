package com.ii.testautomation.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Configuration
@Component
@PropertySource("classpath:EmailSettings.properties")
public class EmailConfiguration {
    @Value("${spring.mail.password}")
    private String emailPassword;
    @Value("${spring.mail.username}")
    private String emailUserName;

    @Bean
    public JavaMailSender javaMailSender()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("pirathaban1992@gmail.com");
        mailSender.setPassword("fwmadbagcxzslvoo");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.transport.protocol", "smtp");

        return mailSender;



    }
}
