package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 9:00
 */
@Repository("alphaImpl")
@Primary
public class AlphaDaoMyBatisImpl implements AlphaDao{

    @Override
    public String select() {
        return "mybatis";
    }
}
