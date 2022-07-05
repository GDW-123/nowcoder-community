package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author GuoDingWei
 * @Date 2022/6/18 13:35
 */

@Component
public class LoginRequiredInterceptor  implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    /**
     *
     * @param request 请求
     * @param response 响应
     * @param handler 拦截的目标
     * @return 返回是否拦截，true为放行，false为拦截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //表示如果拦截的是这个方法的话，那么就可以进行拦截，如果是静态资源的话，那么就不用拦截
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            //拦截方法上面带有@LoginRequired的
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //表示需要登录的请求如果没有登陆的话，就强制跳转到登陆页面上面
            if (loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
