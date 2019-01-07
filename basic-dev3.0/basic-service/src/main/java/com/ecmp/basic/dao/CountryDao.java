package com.ecmp.basic.dao;

import com.ecmp.basic.entity.Country;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * <p/>
 * 实现功能：国家的数据访问接口
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Repository
public interface CountryDao extends BaseEntityDao<Country> {
}
