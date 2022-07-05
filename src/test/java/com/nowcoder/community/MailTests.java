package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Author GuoDingWei
 * @Date 2022/6/15 21:51
 */
@SpringBootTest
//表示以该配置类来启动主程序
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void TestTextMail(){
        mailClient.sendMail("2094881532@qq.com","TEST","Hello World");
    }


    @Test
    public void TestHTMLMail(){
        Context context = new Context();
        context.setVariable("username","GuoDingWei");
        String process = templateEngine.process("/mail/demo", context);
        System.out.println(process);
        mailClient.sendMail("2094881532@qq.com","HTML","Hello World!!!");
    }
}
