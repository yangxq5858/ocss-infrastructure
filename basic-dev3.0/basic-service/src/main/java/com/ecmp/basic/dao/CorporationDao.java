package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Corporation;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：公司的数据访问
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/2 17:22    余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Repository
public interface CorporationDao extends BaseEntityDao<Corporation> {

    /**
     * 根据公司代码查询公司
     *
     * @param code 公司代码
     * @return 公司
     */
    Corporation findByCode(String code);
}
