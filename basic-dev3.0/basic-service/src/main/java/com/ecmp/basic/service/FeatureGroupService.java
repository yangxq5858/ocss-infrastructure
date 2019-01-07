package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureGroupService;
import com.ecmp.basic.dao.FeatureGroupDao;
import com.ecmp.basic.entity.FeatureGroup;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
 * 1.0.00     2017/4/19  19:22    余思豆 (yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Service
public class FeatureGroupService extends BaseEntityService<FeatureGroup> implements IFeatureGroupService {

    @Autowired
    private FeatureGroupDao featureGroupDao;
    @Autowired
    private FeatureService featureService;
    @Override
    protected BaseEntityDao<FeatureGroup> getDao() {
        return featureGroupDao;
    }

    /**
     * 根据功能项组名称模糊查询功能项组
     *
     * @param name 功能项组代码
     * @return 功能项组清单
     */
    @Transactional(readOnly = true)
    @Override
    public List<FeatureGroup> findByNameLike(String name) {
        List<FeatureGroup> featureGroupList = findByFilter(new SearchFilter("name", name, SearchFilter.Operator.LK));
        if (Objects.isNull(featureGroupList)) {
            return Collections.emptyList();
        } else {
            return featureGroupList;
        }
    }

    /**
     * 根据应用模块id查询功能项组
     *
     * @param appModuleId 应用模块id
     * @return 功能项组
     */
    @Transactional(readOnly = true)
    @Override
    public List<FeatureGroup> findByAppModuleId(String appModuleId) {
        SearchFilter searchFilter = new SearchFilter("appModule.id", appModuleId, SearchFilter.Operator.EQ);
        return findByFilter(searchFilter);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (featureService.isExistsByProperty("featureGroup.id",s)){
            //该组下存在功能项，禁止删除！
            return OperateResult.operationFailure("00014");
        }
        return super.preDelete(s);
    }
}
