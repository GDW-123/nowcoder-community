package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 23:17
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 配置拦截规则，这个方法是在controller之前执行的
     * @param request 请求数据，我们的cookie就是通过request来得到的，
     *                因此这个没有cookie，我们依然可以根据request来得到cookie
     * @param response 响应数据
     * @param handler 拦截的方法，比如注册、登录等一些方法
     * @return 返回布尔类型，只有返回true的时候才放行，否则会被拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                //由于服务器对浏览器是一对多的关系，因此每个用户的信息都要独立保存
                //使用ThreadLocal本地线程变量可以保证多线程的环境下来使用这一份数据
                hostHolder.setUser(user);
                // 构建用户认证的结果,并存入SecurityContext,以便于Security进行授权.
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        return true;
    }

    /**
     * 由于user对象在controller之后，模板引擎之前调用，因此我们可以实现postHandle方法
     * 在执行了controller之后，我们将user对象放到model中，然后结合模板引擎，返回一个动态页面
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //从ThreadLocal本地线程变量中获取user对象
        User user = hostHolder.getUser();
        //将这个user对象放到model中
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * afterCompletion是在模板引擎之后来进行调用的，
     * 我们可以在使用了模板引擎之后就不需要user对象了，
     * 这个时候就可以将hostHolder中的user对象清理掉，
     * 防止内存泄漏
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        //表示在退出的时候就将这个token中的认证的权限清除掉
        //SecurityContextHolder.clearContext();
    }
}
