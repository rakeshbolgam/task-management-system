package com.rakesh.task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("taskmanager.rakhii@gmail.com");
        mailSender.setPassword("hzkw pajg qbze jeim");
//        mailSender.setPassword("ztbs xttk wdtt efuc");
        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.transport.protocol","smtp");
        javaMailProperties.put("mail.smtp.auth",true);
        javaMailProperties.put("mail.smtp.starttls.enable",true);
        javaMailProperties.put("mail.debug",true);

        return mailSender;
    }
}
