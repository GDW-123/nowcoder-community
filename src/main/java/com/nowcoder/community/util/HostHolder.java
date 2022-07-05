package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 23:28
 */

/**
 * 用来实现多线程下的数据的隔离性的
 * 使用一个ThreadLocal本地线程变量来存储这个用户对象，
 * 可以实现多个用户隔离访问同一个用户对象
 * 我们查看了ThreadLocal的源码，发现它是根据线程来存储数据的，
 * 因此每个线程存储的数据是不一样的，即以线程为key来存取值的
 */
@Component
public class HostHolder {

    //ThreadLocal初始化
    private ThreadLocal<User> users = new ThreadLocal<>();

    //数据需要先设置，然后才能访问，否则就会报错的
    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    //为了防止内存泄漏，所以需要将这个对象清除
    public void clear() {
        users.remove();
    }
}
