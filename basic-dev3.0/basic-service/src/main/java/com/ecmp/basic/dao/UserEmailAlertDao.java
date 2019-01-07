package com.ecmp.basic.dao;

import com.ecmp.basic.entity.UserEmailAlert;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.stereotype.Repository;

/**
 * Created by WangShuFa on 2018/7/11.
 */
@Repository
public interface UserEmailAlertDao extends BaseEntityDao<UserEmailAlert>,UserEmailAlertExtDao{
}
