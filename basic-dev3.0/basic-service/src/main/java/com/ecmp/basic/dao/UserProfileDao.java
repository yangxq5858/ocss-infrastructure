package com.ecmp.basic.dao;

import com.ecmp.basic.entity.UserProfile;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户配置数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 15:45            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface UserProfileDao extends BaseEntityDao<UserProfile>, UserProfileExtDao {

    /**
     * 根据用户的id查询用户配置
     *
     * @param userId 用户id
     * @return 用户配置
     */
    UserProfile findByUserId(String userId);
}
