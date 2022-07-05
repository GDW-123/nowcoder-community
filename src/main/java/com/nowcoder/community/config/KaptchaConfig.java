package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 14:21
 */

/**
 * 用于登录的时候实现验证码
 * 我们使用java也可以来实现验证码的功能，
 * 但是我们现在使用的是kaptcha工具
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer() {

        //用于生成验证码的外观形式
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100");//边框的宽度
        properties.setProperty("kaptcha.image.height", "40");//边框的高度
        properties.setProperty("kaptcha.textproducer.font.size", "32");//字体
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");//字的颜色
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");//字符的范围
        properties.setProperty("kaptcha.textproducer.char.length", "4");//字符的长度
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        //生成验证码
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
