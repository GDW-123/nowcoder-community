package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author GuoDingWei
 * @Date 2022/6/15 21:43
 */
@Component
public class MailClient {

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    //表示发件人，这个是固定不变的
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 表示邮件的发送
     * @param to 表示把邮件发送给谁
     * @param subject 表示主题
     * @param content 表示内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            //设置发件人
            helper.setFrom(from);
            //设置收件人
            helper.setTo(to);
            //设置主题
            helper.setSubject(subject);
            //设置内容，true表示支持html
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败:" + e.getMessage());
        }
    }
}