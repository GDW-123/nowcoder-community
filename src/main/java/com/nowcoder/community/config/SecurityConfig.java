
package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import java.io.PrintWriter;


/**
 * @Author GuoDingWei
 * @Date 2022/6/24 19:21
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {


    /**
     * 忽略掉对静态资源的拦截
     * @param web
     * @throws Exception
     */

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }


    /**
     * 进行授权
     * 对所有的controller中的路径进行授权
     * @param http http请求
     * @throws Exception
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 对下面出现的路径进行授权，只要石下面的路径都是可以访问的
        //这些路径都是登录以后就可以访问的，不论登陆以后是什么权限，都是可以来访问的
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                //这些路径都是登录以后就可以访问的，不论登陆以后是什么权限，都是可以来访问的
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/delete",
                        "/data/**",
                        "/actuator/**"
                )
                .hasAnyAuthority(
                        AUTHORITY_ADMIN
                )
                //除了上述的路径以外的其他路径都是可以直接访问的,就有说即使不登陆也是可以来访问的
                .anyRequest().permitAll()
                //禁用csrf
                .and().csrf().disable();

        // 权限不够时的处理
        http.exceptionHandling()
                // 没有登录的时候显示给用户的方式
                .authenticationEntryPoint((request, response, e) -> {
                    String xRequestedWith = request.getHeader("x-requested-with");
                    //异步请求，返回的是json字符串
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJSONString(403, "你还没有登录哦!"));
                        //同步请求，直接重定向到相应的页面上面
                    } else {
                        response.sendRedirect(request.getContextPath() + "/login");
                    }
                })
                // 权限不足的时候显示给用户的方式
                .accessDeniedHandler((request, response, e) -> {
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限!"));
                    } else {
                        response.sendRedirect(request.getContextPath() + "/denied");
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理.
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码.
        http.logout().logoutUrl("/securitylogout");
    }
}
