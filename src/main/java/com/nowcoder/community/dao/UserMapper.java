package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:20
 */
//这两个注解其实都是可以的，只不过我们在使用mybatis的时候习惯使用的是Mapper注解
//作用都是让spring来管理这个bean
//@Repository
@Mapper
public interface UserMapper {

    /**
     * 根据id进行查询用户
     * @param id 传入的参数是用户id（条件）
     * @return 返回值是这个用户对象（里面都包含有用户属性）
     */
    User selectById(int id);

    /**
     * 根据用户名查询用户
     * @param username 传入的参数是用户名
     * @return 返回值是这个用户对象
     */
    User selectByName(String username);

    /**
     * 根据邮箱查询用户
     * @param email 传入的参数是邮箱
     * @return 返回值是这个用户对象
     */
    User selectByEmail(String email);

    /**
     * 插入用户（每次注册的时候都会进行用户的增加）
     * @param user 传入的参数是用户对象
     * @return 返回值是插入成功的条数
     */
    int insertUser(User user);

    /**
     * 更新登录状态
     * @param id 传入的参数是用户的id（条件）
     * @param status 传入的参数是用户需要修改的状态
     * @return 返回值是更新状态成功的条数
     */
    int updateStatus(int id,int status);

    /**
     * 更新用户的头像（就是用户来更新头像）
     * @param id 传入的参数是用户的id（条件）
     * @param headerUrl 传入的参数是用户的头像地址
     * @return 返回值是更新头像成功的条数
     */
    int updateHeader(int id,String headerUrl);

    /**
     * 修改密码
     * @param id 用户的id
     * @param password 新修改的密码
     * @return 返回执行成功的条数
     */
    int updatePassword(int id,String password);

}
