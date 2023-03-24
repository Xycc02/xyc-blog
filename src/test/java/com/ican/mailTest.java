package com.ican;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @Description: mailTest
 * @Author XYC
 * @Date: 2023/3/22 22:59
 * @Version 1.0
 */

@SpringBootTest
public class mailTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;


    @Test
    void simpleMail() {
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setFrom(email);
        simpleMail.setTo("2672424338@qq.com");
        simpleMail.setSubject("测试邮件");
        simpleMail.setText("你好!!!");
        javaMailSender.send(simpleMail);
    }
}
