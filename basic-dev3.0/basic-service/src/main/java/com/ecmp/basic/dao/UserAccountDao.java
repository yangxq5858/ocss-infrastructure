package com.ecmp.basic.dao;

import com.ecmp.basic.entity.UserAccount;
import com.ecmp.enums.UserType;
import com.ecmp.core.dao.BaseEntityDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户帐号的数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 14:32            高银军                  新建
 * 1.0.00      2017/4/25 19:26            秦有宝                  增加方法(findByAccount，findByAccountAndTenantCode)
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface UserAccountDao extends BaseEntityDao<UserAccount>, UserAccountExtDao {

    /**
     * 根据用户账号查询用户账户
     *
     * @param account 用户账号
     * @return 用户账户列表
     */
    List<UserAccount> findByAccount(String account);

    /**
     * 根据用户账号和租户代码查询用户账户
     *
     * @param account 用户账号
     * @param tenantCode 租户代码
     * @return 用户账户列表
     */
    UserAccount findByAccountAndTenantCode(String account,String tenantCode);

    /**
     * 根据用户类型和租户代码查询用户帐号
     *
     * @param tenantCode 租户代码
     * @param userType 用户类型
     *
     * @return 用户帐号清单
     */
    List<UserAccount> findByTenantCodeAndUserUserType(String tenantCode, UserType userType);

    /**
     * 根据用户Id查询用户帐号
     *
     * @param userId 用户id
     * @return 用户帐号清单
     */
    List<UserAccount> findByUserId(String userId);
}
