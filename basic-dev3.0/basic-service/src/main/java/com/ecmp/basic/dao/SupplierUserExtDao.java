package com.ecmp.basic.dao;

import com.ecmp.basic.entity.SupplierUser;

/**
 * 供应商用户扩展DAO
 * Author:jamson
 * date:2018/3/8
 */
public interface SupplierUserExtDao {
    
    /**
     * 保存供应商用户
     *
     * @param entity 供应商用户实体
     * @param isNew  是否是创建
     * @return 保存结果
     */
    SupplierUser save(SupplierUser entity, boolean isNew);

}
