package com.ecmp.basic.service;

import com.ecmp.basic.api.ITenantService;
import com.ecmp.basic.dao.TenantDao;
import com.ecmp.basic.entity.Tenant;
import com.ecmp.context.util.NumberGenerator;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现租户的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 15:45            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class TenantService extends BaseEntityService<Tenant> implements ITenantService {

    @Autowired
    private TenantDao tenantDao;

    @Override
    protected BaseEntityDao<Tenant> getDao() {
        return tenantDao;
    }

    @Override
    public OperateResultWithData<Tenant> save(Tenant entity) {
        if (StringUtils.isBlank(entity.getCode())) {
            entity.setCode(NumberGenerator.getNumber(Tenant.class));
        }
        return super.save(entity);
    }

    /**
     * 是否冻结
     *
     * @param tenantCode 租户代码
     * @return 返回true，则已被冻结；反之正常
     */
    public boolean isFrozen(String tenantCode) {
        Tenant tenant = tenantDao.findByFrozenFalseAndCode(tenantCode);
        return Objects.isNull(tenant);
    }
}
