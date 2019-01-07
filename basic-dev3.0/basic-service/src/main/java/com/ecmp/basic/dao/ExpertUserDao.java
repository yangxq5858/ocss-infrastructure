package com.ecmp.basic.dao;

import com.ecmp.basic.entity.ExpertUser;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * 专家用户数据访问层
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/3/6 20:21
 */
@Repository
public interface ExpertUserDao extends BaseEntityDao<ExpertUser>, ExpertUserExtDao {
    /**
     * 根据专家用户中的专家的ID查询专家用户
     *
     * @param expertId 专家用户中的专家的ID
     * @return 专家用户
     */
    ExpertUser findByExpertId(String expertId);
}
