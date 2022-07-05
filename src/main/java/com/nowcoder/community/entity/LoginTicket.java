package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 15:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginTicket {

    //id
    private int id;
    //用户id
    private int userId;
    //登录凭证
    private String ticket;
    //登录状态
    private int status;
    //创建日期
    private Date expired;
}
