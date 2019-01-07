package com.ecmp.core.dao;

import com.ecmp.core.dao.jpa.BaseDao;
import com.ecmp.core.entity.BaseEntity;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <strong>实现功能:</strong>
 * <p></p>
 *
 * @param <T> BaseEntity的子类
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/5/4 15:03
 */
@NoRepositoryBean
public interface BaseEntityDao<T extends BaseEntity> extends BaseDao<T, String> {
}
