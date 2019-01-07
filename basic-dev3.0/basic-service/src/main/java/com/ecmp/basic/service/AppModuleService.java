package com.ecmp.basic.service;

import com.ecmp.basic.api.IAppModuleService;
import com.ecmp.basic.dao.AppModuleDao;
import com.ecmp.basic.dao.FeatureGroupDao;
import com.ecmp.basic.entity.AppModule;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * 应用模块Entity定义
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00     2017/4/19  19:00     余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Service
public class AppModuleService extends BaseEntityService<AppModule> implements IAppModuleService {

    @Autowired
    private AppModuleDao appModuleDao;
    @Autowired
    private FeatureGroupDao featureGroupDao;
    @Autowired
    private TenantAppModuleService tenantAppModuleService;

    @Override
    protected BaseEntityDao<AppModule> getDao() {
        return appModuleDao;
    }

    @Override
    public OperateResultWithData<AppModule> save(AppModule entity) {
        Integer rank = entity.getRank();
        if (Objects.isNull(rank) || rank < 1) {
            return OperateResultWithData.operationFailure("00012");
        }
        return super.save(entity);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (featureGroupDao.isExistsByProperty("appModule.id", s)) {
            //00018 = 该应用模块下存在功能项组，禁止删除！
            return OperateResult.operationFailure("00018");
        }
        if (tenantAppModuleService.isExistsByProperty("child.id", s)) {
            //该应用模块已分配给租户，禁止删除！
            return OperateResult.operationFailure("00046");
        }
        return super.preDelete(s);
    }

    /**
     * 通过代码查询应用模块
     *
     * @param code 应用模块代码
     * @return 应用模块
     */
    @Override
    public AppModule findByCode(String code) {
        AppModule appModule = appModuleDao.findByCode(code);
        appModule.setWebBaseAddress(ContextUtil.getGlobalProperty(appModule.getWebBaseAddress()));
        appModule.setApiBaseAddress(ContextUtil.getGlobalProperty(appModule.getApiBaseAddress()));
        return appModule;
    }
}
