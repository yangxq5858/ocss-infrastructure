package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.UserEmailAlertExtDao;
import com.ecmp.basic.entity.UserEmailAlert;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by WangShuFa on 2018/7/12.
 * 用户邮件提醒DAO接口
 */
public class UserEmailAlertDaoImpl extends BaseEntityDaoImpl<UserEmailAlert> implements UserEmailAlertExtDao {
    /**
     * 构造函数
     *
     * @param entityManager 业务实体管理器
     */
    public UserEmailAlertDaoImpl(EntityManager entityManager) {
        super(UserEmailAlert.class, entityManager);
    }

    @Override
    public List<UserEmailAlert> findByUserIds(List<String> userIdList) {
        String queryStr = "select r from UserEmailAlert r " +
                "where r.userId in :userIdList ";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("userIdList", userIdList);
        return query.getResultList();
    }


}
