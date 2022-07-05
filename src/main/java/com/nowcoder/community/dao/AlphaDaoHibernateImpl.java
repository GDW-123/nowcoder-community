package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @Author GuoDingWei
 * @Date 2022/6/14 15:21
 */
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
