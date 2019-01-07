package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.ExpertUserExtDao;
import com.ecmp.basic.entity.ExpertUser;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import com.ecmp.core.entity.ITenant;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * 专家用户扩展接口实现类
 * Author:jamson
 * date:2018/3/13
 */
public class ExpertUserDaoImpl extends BaseEntityDaoImpl<ExpertUser> implements ExpertUserExtDao {
    public ExpertUserDaoImpl(EntityManager entityManager) {
        super(ExpertUser.class, entityManager);
    }

    @Override
    public ExpertUser save(ExpertUser entity, boolean isNew) {
        if (entity != null) {
            Date now = new Date();
            String userId = ContextUtil.getUserId();
            String userAccount = ContextUtil.getUserAccount();
            String userName = ContextUtil.getUserName();
            //创建
            if (isNew) {
                entity.setCreatorId(userId);
                entity.setCreatorName(userName);
                entity.setCreatorAccount(userAccount);
                entity.setCreatedDate(now);
            }
            entity.setLastEditorId(userId);
            entity.setLastEditorName(userName);
            entity.setLastEditorAccount(userAccount);
            entity.setLastEditedDate(now);
        }
        //是否是租户实体(只是在租户代码为空时设置)
        if (entity != null && StringUtils.isBlank(((ITenant) entity).getTenantCode())) {
            //从上下文中获取租户代码
            ITenant tenant = (ITenant) entity;
            tenant.setTenantCode(ContextUtil.getTenantCode());
        }
        if (isNew) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}
