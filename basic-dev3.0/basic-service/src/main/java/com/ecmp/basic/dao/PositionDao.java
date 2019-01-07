package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Position;
import com.ecmp.core.dao.BaseEntityDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位的数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 23:02            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface PositionDao extends BaseEntityDao<Position>, PositionExtDao {
    /**
     * 根据岗位类别的id来查询岗位
     *
     * @param categoryId 岗位类别Id
     * @return 岗位清单
     */
    List<Position> findByPositionCategoryId(String categoryId);

    /**
     * 根据组织机构的id获取岗位
     *
     * @param organizationId 组织机构的id
     * @return 岗位清单
     */
    List<Position> findByOrganizationId(String organizationId);
}
