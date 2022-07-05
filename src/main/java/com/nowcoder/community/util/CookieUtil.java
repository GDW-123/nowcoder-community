package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 23:21
 */

//工具类，获取cookie中的值
public class CookieUtil {

    /**
     * 获取cookie中的值
     * @param request 这里给的不是cookie，我们可以根据request
     *                来获取cookie，然后来取cookie中的值
     * @param name 由于cookie中存储的数据都是k-v键值对，
     *             因此我们需要通过key来取value
     * @return 返回cookie中的值
     */
    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空!");
        }
        //通过request来获取cookie，得到的是一个cookie数组
        Cookie[] cookies = request.getCookies();
        //现在来遍历这个cookie，去找需要的name对应的值
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        //找不到的话就返回空
        return null;
    }
}
