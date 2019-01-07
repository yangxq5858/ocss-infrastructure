package com.ecmp.basic.dao;

import com.ecmp.basic.entity.SupplierUser;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * 供应商用户数据访问层
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/3/6 20:21
 */
@Repository
public interface SupplierUserDao extends BaseEntityDao<SupplierUser>, SupplierUserExtDao {
    /**
     * 根据供应商用户的代码与供应商用户中的供应商ID查询供应商用户
     *
     * @param code       供应商用户的代码
     * @param supplierId 供应商用户中的供应商ID
     * @return 供应商用户
     */
    SupplierUser findByCodeAndSupplierId(String code, String supplierId);
}
