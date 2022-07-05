package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:19
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    //用户id
    private int id;
    //用户名
    private String username;
    //用户密码
    private String password;
    //加密盐
    private String salt;
    //邮箱
    private String email;
    //用户角色
    private int type;
    //登录状态
    private int status;
    //激活码
    private String activationCode;
    //头像的访问路径
    private String headerUrl;
    //用户创建时间
    private Date createTime;

}
