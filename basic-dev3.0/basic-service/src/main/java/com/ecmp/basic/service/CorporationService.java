package com.ecmp.basic.service;

import com.ecmp.basic.api.ICorporationService;
import com.ecmp.basic.dao.CorporationDao;
import com.ecmp.basic.entity.Corporation;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：公司业务逻辑实现
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/2 17:26    余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Service
public class CorporationService extends BaseEntityService<Corporation> implements ICorporationService {

    @Autowired
    private CorporationDao corporationDao;

    @Override
    protected BaseEntityDao<Corporation> getDao() {
        return corporationDao;
    }

    /**
     * 根据公司代码查询公司
     *
     * @param code 公司代码
     * @return 公司
     */
    @Override
    public Corporation findByCode(String code) {
        return corporationDao.findByCode(code);
    }
}
