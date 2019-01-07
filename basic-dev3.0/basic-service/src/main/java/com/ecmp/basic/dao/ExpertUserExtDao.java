package com.ecmp.basic.dao;

import com.ecmp.basic.entity.ExpertUser;

/**
 * 专家用户扩展接口
 * Author:jamson
 * date:2018/3/13
 */
public interface ExpertUserExtDao {
    /**
     * 保存专家用户
     *
     * @param entity 专家用户实体
     * @param isNew  是否是创建
     * @return 保存结果
     */
    ExpertUser save(ExpertUser entity, boolean isNew);
}
