package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureService;
import com.ecmp.basic.dao.FeatureDao;
import com.ecmp.basic.dao.FeatureGroupDao;
import com.ecmp.basic.dao.MenuDao;
import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.FeatureGroup;
import com.ecmp.basic.entity.Menu;
import com.ecmp.basic.entity.Tenant;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：功能项的API服务实现类
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间                  变更人                 变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/4/20 9:16              李汶强                   新建
 * 1.0.00      2017/5/10 17:58             高银军                   修改
 * <br>
 * *************************************************************************************************<br>
 */
@Service
public class FeatureService extends BaseEntityService<Feature> implements IFeatureService {

    @Autowired
    private FeatureDao featureDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private FeatureGroupDao featureGroupDao;

    @Override
    protected BaseEntityDao<Feature> getDao() {
        return featureDao;
    }

    /**
     * 通过代码获取功能项
     *
     * @param code 功能项代码
     * @return 功能项
     */
    public Feature findByCode(String code) {
        return featureDao.findByCode(code);
    }

    /**
     * 根据功能项组id查询功能项
     *
     * @param featureGroupId 功能项组的id
     * @return 功能项清单
     */
    @Transactional(readOnly = true)
    @Override
    public List<Feature> findByFeatureGroupId(String featureGroupId) {
        if (StringUtils.isBlank(featureGroupId)) {
            return Collections.emptyList();
        } else {
            SearchFilter searchFilter = new SearchFilter("featureGroup.id", featureGroupId, SearchFilter.Operator.EQ);
            return findByFilter(searchFilter);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Feature> findByAppModuleId(String appModuleId) {
        if (StringUtils.isNotBlank(appModuleId)) {
            SearchFilter searchFilter;
            searchFilter = new SearchFilter("appModule.id", appModuleId, SearchFilter.Operator.EQ);
            List<FeatureGroup> featureGroupList = featureGroupDao.findByFilter(searchFilter);
            if (CollectionUtils.isNotEmpty(featureGroupList)) {
                List<Feature> featureList = new ArrayList<>();
                for (FeatureGroup featureGroup : featureGroupList) {
                    searchFilter = new SearchFilter("featureGroup.id", featureGroup.getId(), SearchFilter.Operator.EQ);
                    List<Feature> features = featureDao.findByFilter(searchFilter);
                    if (CollectionUtils.isNotEmpty(features)) {
                        featureList.addAll(features);
                    }
                }
                return featureList;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取租户已经分配的应用模块对应的功能项清单
     *
     * @param tenant 租户
     * @return 功能项清单
     */
    @Transactional(readOnly = true)
    public List<Feature> getTenantCanUseFeatures(Tenant tenant) {
        if (Objects.isNull(tenant)) {
            return Collections.emptyList();
        }
        return featureDao.getTenantCanUseFeatures(tenant.getId());
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        List<Menu> menus = menuDao.findByFeatureId(s);
        if (menus != null && menus.size() > 0) {
            //该功能项存在菜单，禁止删除！
            return OperateResult.operationFailure("00015");
        }
        return super.preDelete(s);
    }
}
