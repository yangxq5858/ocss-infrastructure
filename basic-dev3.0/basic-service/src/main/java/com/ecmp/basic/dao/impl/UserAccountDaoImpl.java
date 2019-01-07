package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.UserAccountExtDao;
import com.ecmp.basic.entity.UserAccount;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import com.ecmp.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户账户扩展接口实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/6/13 15:18      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class UserAccountDaoImpl extends BaseEntityDaoImpl<UserAccount> implements UserAccountExtDao {

    public UserAccountDaoImpl(EntityManager entityManager) {
        super(UserAccount.class, entityManager);
    }

    /**
     * 检查账号是否存在
     *
     * @param account 账号
     * @param id 实体id
     * @return 是否存在
     */
    @Override
    public Boolean isAccountExist(String account, String id) {
        if(StringUtils.isBlank(id)){
            id = IdGenerator.uuid();
        }
        String sql = "select r.id from UserAccount r where r.account=:account " +
                "and r.tenantCode=:tenantCode "+
                "and r.id<>:id ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("account", account);
        query.setParameter("tenantCode", ContextUtil.getTenantCode());
        query.setParameter("id",id);
        List results = query.getResultList();
        return !results.isEmpty();
    }
}