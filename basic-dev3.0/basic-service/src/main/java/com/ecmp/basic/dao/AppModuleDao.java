package com.ecmp.basic.dao;

import com.ecmp.basic.entity.AppModule;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * 应用模块Entity定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 16:28     余思豆(yusidou)                 新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface AppModuleDao extends BaseEntityDao<AppModule> {

    /**
     * 通过代码查询应用模块
     *
     * @param code 应用模块代码
     * @return 应用模块
     */
    AppModule findByCode(String code);
}
