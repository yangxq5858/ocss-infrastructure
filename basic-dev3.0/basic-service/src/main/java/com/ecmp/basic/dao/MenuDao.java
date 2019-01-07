package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Menu;
import com.ecmp.core.dao.BaseTreeDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现系统菜单数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 16:44              李汶强                  新建
 * 1.0.00      2017/5/10 17:58             高银军                   修改
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface MenuDao extends BaseTreeDao<Menu> {

    /**
     * 用名称模糊查询
     * @param name 查询关键字
     * @return 查询结果集
     */
    List<Menu> findByNameLike(String name);

    /**
     * 通过功能项查询
     * @param featureId 查询关键字
     * @return 查询结果集
     */
    List<Menu> findByFeatureId(String featureId);

}
