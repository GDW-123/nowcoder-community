package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 21:25
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    //@Autowired
    //private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    /**
     * 配置拦截路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //这里表示按照刚刚配置的拦截规则来进行拦截
        //如果只写registry.addInterceptor(alphaInterceptor)，那么就会拦截一切请求
        //项目中除了访问controller，还有很多css，js等文件也是可以进行拦截的，
        //但是我们一般也不会拦截这些资源，一般拦截的都是请求
        registry.addInterceptor(alphaInterceptor)
                //这里表示哪些路径不拦截（即排除哪些路径）
                .excludePathPatterns("**/*.css","**/*.js","**/*.png","**/*.jpg","**/*.jpeg")
                //表示具体拦截的路径
                .addPathPatterns("/register","/login");

        //登录的拦截
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        //registry.addInterceptor(loginRequiredInterceptor)
                //.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
