package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Region;
import com.ecmp.core.dao.BaseTreeDao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

/**
 * <p/>
 * 实现功能：行政区域的数据访问接口
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Repository
public interface RegionDao extends BaseTreeDao<Region> {

    /**
     * 通过国家id查询行政区域树
     *
     * @param countryId 国家id
     * @return 行政区域树
     */
    Region findByCountryIdAndNodeLevel(String countryId,Integer nodeLevel);

    /**
     * Retrieves an entity by its id.
     *
     * @param s must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @EntityGraph(attributePaths = {"country"}, type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Region findOne(String s);
}
