package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.UserProfileExtDao;
import com.ecmp.basic.entity.UserProfile;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import com.ecmp.core.search.SearchFilter;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/6/15 17:01      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class UserProfileDaoImpl extends BaseEntityDaoImpl<UserProfile> implements UserProfileExtDao {

    public UserProfileDaoImpl(EntityManager entityManager) {
        super(UserProfile.class, entityManager);
    }

    /**
     * 根据用户id列表获取用户配置
     *
     * @param userIds 用户id集合
     */
    @Override
    public List<UserProfile> findNotifyInfoByUserIds(List<String> userIds) {
        if (!CollectionUtils.isEmpty(userIds)) {
            SearchFilter filter = new SearchFilter("user.id", userIds, SearchFilter.Operator.IN);
            return findByFilter(filter);
        } else {
            return null;
        }
    }
}