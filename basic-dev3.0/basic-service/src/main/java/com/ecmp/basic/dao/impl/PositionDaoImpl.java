package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.PositionExtDao;
import com.ecmp.basic.entity.Position;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位扩展接口实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/6/30 13:48      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class PositionDaoImpl extends BaseEntityDaoImpl<Position> implements PositionExtDao {

    public PositionDaoImpl(EntityManager entityManager) {
        super(Position.class, entityManager);
    }

    /**
     * 检查同一部门下的岗位名称是否存在
     *
     * @param organizationId 组织机构的id
     * @param name           岗位名称
     * @param id             实体id
     * @return 是否存在
     */
    @Override
    public Boolean isOrgAndNameExist(String organizationId, String name, String id) {
        if(StringUtils.isBlank(id)){
            id = IdGenerator.uuid();
        }
        Search search = new Search();
        search.addFilter(new SearchFilter("organization.id", organizationId, SearchFilter.Operator.EQ));
        search.addFilter(new SearchFilter("name", name, SearchFilter.Operator.EQ));
        search.addFilter(new SearchFilter("id", id, SearchFilter.Operator.NE));
        List results = findByFilters(search);
        return !results.isEmpty();
    }
}