package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Author GuoDingWei
 * @Date 2022/6/17 15:15
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {

    /**
     * 往数据库中插入凭证
     * @param loginTicket 凭证对象
     * @return 返回成功插入的条数
     */
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据凭证来进行查询
     * @param ticket 凭证
     * @return 返回凭证对象
     */
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新状态
     * @param ticket 凭证
     * @param status 状态
     * @return 返回成功更新的条数
     */
    int updateStatus(String ticket, int status);
}
