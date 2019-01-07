package com.ecmp.basic.dao;

import com.ecmp.basic.entity.User;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 9:40        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */

@Repository
public interface UserDao extends BaseEntityDao<User> {

    /**
     * 根据用户id查询用户
     *
     * @param id 用户id
     * @return 用户
     */
    User getById(String id);
}

