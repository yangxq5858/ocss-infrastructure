package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserAccountService;
import com.ecmp.basic.dao.UserAccountDao;
import com.ecmp.basic.dao.UserDao;
import com.ecmp.basic.dao.UserProfileDao;
import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.UserAccount;
import com.ecmp.basic.entity.UserProfile;
import com.ecmp.basic.entity.vo.MenuVo;
import com.ecmp.basic.entity.vo.UserAccountVo;
import com.ecmp.basic.entity.vo.UserPasswordVo;
import com.ecmp.config.util.IgnoreCheckSession;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.enums.UserType;
import com.ecmp.log.util.LogUtil;
import com.ecmp.util.IdGenerator;
import com.ecmp.vo.LoginStatus;
import com.ecmp.vo.OperateResultWithData;
import com.ecmp.vo.SessionUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现用户帐号的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 14:34            高银军                  新建
 * 1.0.00      2017/4/25 19:26            秦有宝                  增加登陆方法(login)
 * <p/>
 * *************************************************************************************************
 */
@SuppressWarnings("unchecked")
@Service
public class UserAccountService extends BaseEntityService<UserAccount> implements IUserAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);

    @Autowired
    private UserAccountDao userAccountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TenantService tenantService;

    @Override
    protected BaseEntityDao<UserAccount> getDao() {
        return userAccountDao;
    }

    /**
     * 新建员工时调用此方法
     *
     * @param entity 用户帐号实体
     * @return 操作后的结果
     */
    @Override
    public OperateResultWithData<UserAccount> save(UserAccount entity) {
        User user;
        boolean isNew = entity.isNew();
        //在user表中插入一条数据。
        if (isNew) {
            //新增用户
            user = new User();
            user.setUserName(entity.getUserName());
            user.setUserType(entity.getUserType());
            user.setTenantCode(entity.getTenantCode());
            UserAuthorityPolicy userAuthorityPolicy = entity.isCreateAdmin() ? UserAuthorityPolicy.TenantAdmin : UserAuthorityPolicy.NormalUser;
            user.setUserAuthorityPolicy(userAuthorityPolicy);
            userDao.save(user);

            //新增用户配置
            UserProfile userProfile = new UserProfile();
            userProfile.setUser(user);
            userProfile.setEmail(entity.getEmail());
            userProfileDao.save(userProfile);

            entity.setSalt(IdGenerator.uuid());    //设置加密盐值
            entity.setPassword(DigestUtils.md5Hex(entity.getPassword() + entity.getSalt()));  //加密
            entity.setUser(user);
        } else {
            //修改用户
            user = userDao.getById(entity.getUser().getId());
            user.setUserName(entity.getUserName());
            user.setUserType(entity.getUserType());
            userDao.save(user);

            //修改用户账户,设置可以修改的属性
            UserAccount account = findOne(entity.getId());
            account.setNickName(entity.getNickName());
            entity = account;
        }
        entity.setUser(user);
        return super.save(entity);
    }

    /**
     * 保存用户帐号(个人设置中调用此方法)
     *
     * @param userAccount 用户帐号
     * @return 保存后的用户帐号
     */
    @Override
    public OperateResultWithData<UserAccount> saveAccount(UserAccount userAccount) {
        Boolean isNew = userAccount.isNew();
        //检查该租户下员工账号不能重复
        if (userAccountDao.isAccountExist(userAccount.getAccount(), userAccount.getId())) {
            //00041= 该账号【{0}】已存在，请重新输入！
            return OperateResultWithData.operationFailure("00041", userAccount.getAccount());
        }
        if (isNew) {
            User user = new User();
            user.setId(ContextUtil.getUserId());
            String salt = IdGenerator.uuid();
            userAccount.setSalt(salt);
            userAccount.setUser(user);
            userAccount.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(ContextUtil.getGlobalProperty("ECMP_DEFAULT_USER_PASSWORD")) + salt));
        } else {
            UserAccount account = findOne(userAccount.getId());
            account.setAccount(userAccount.getAccount());
            account.setNickName(userAccount.getNickName());
            userAccount = account;
        }
        return super.save(userAccount);
    }

    /**
     * 根据用户类型和租户代码查询用户帐号
     *
     * @param tenantCode 租户代码
     * @param userType   用户类型
     * @return 用户帐号清单
     */
    @Override
    public List<UserAccount> findByTenantCodeAndUserUserType(String tenantCode, UserType userType) {
        return userAccountDao.findByTenantCodeAndUserUserType(tenantCode, userType);
    }

    /**
     * 根据用户账号和租户代码查询用户账户
     *
     * @param account    用户账号
     * @param tenantCode 租户代码
     * @return 用户账户列表
     */
    @Override
    @IgnoreCheckSession
    public UserAccount findByAccountAndTenantCode(String account, String tenantCode) {
        return userAccountDao.findByAccountAndTenantCode(account, tenantCode);
    }

    /**
     * 修改用户帐号密码
     *
     * @param userPasswordVo 用户帐号
     * @return 修改密码后的用户帐号
     */
    @Override
    public OperateResultWithData<UserAccount> updatePassword(UserPasswordVo userPasswordVo) {
        UserAccount userAccount = userAccountDao.findOne(userPasswordVo.getUserAccountId());
        if (userAccount == null) {
            //00053= 账号【{0}】不存在，修改失败！
            return OperateResultWithData.operationFailure("00053", userPasswordVo.getAccount());
        }
        if (!DigestUtils.md5Hex(DigestUtils.md5Hex(userPasswordVo.getOldPassword()) + userAccount.getSalt()).equals(userAccount.getPassword())) {
            //00051= 旧密码输入错误，请重新输入！
            return OperateResultWithData.operationFailure("00051");
        }
        if (!userPasswordVo.getNewPassword().equals(userPasswordVo.getConfirmNewPassword())) {
            //00052= 新密码输入不一致，请重新输入！
            return OperateResultWithData.operationFailure("00052");
        }
        userAccount.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(userPasswordVo.getNewPassword()) + userAccount.getSalt()));
        return super.save(userAccount);
    }

    /**
     * 根据用户Id查询用户帐号
     *
     * @param userId 用户id
     * @return 操作结果
     */
    @Override
    public List<UserAccountVo> findByUserId(String userId) {
        List<UserAccount> userAccounts = userAccountDao.findByUserId(userId);
        List<UserAccountVo> userAccountVos = new ArrayList<>();
        //通过用户账户生成展示对象
        userAccounts.forEach((m) -> {
            UserAccountVo vo = new UserAccountVo(m);
            userAccountVos.add(vo);
        });
        return userAccountVos;
    }

    /**
     * 用户登陆
     *
     * @param appId      应用标识
     * @param tenantCode 租户代码
     * @param account    账号
     * @param password   密码（MD5散列值）
     * @return 用户信息
     */
    @Override
    @IgnoreCheckSession
    public SessionUser login(String appId, String tenantCode, String account, String password) {
        LOGGER.info("call login ,params are {},{},{}", appId, tenantCode, account);
        UserAccount userAccount = null;
        SessionUser sessionUser = new SessionUser();
        sessionUser.setLoginStatus(LoginStatus.failure);
        List<UserAccount> userAccounts = userAccountDao.findByAccount(account);
        LOGGER.debug("get userAccounts is {}", userAccount);
        if (CollectionUtils.isEmpty(userAccounts)) {
            //返回状态， 登陆状态：1：登陆成功 2：登陆失败  3：多租户，需要传入租户代码
            //00002 = 登陆失败，账号不存在！
            return sessionUser;
        }
        //多租户存在同一账号
        else if (userAccounts.size() > 1) {
            sessionUser.setLoginStatus(LoginStatus.multiTenant);
            //多租户
            if (StringUtils.isNotBlank(tenantCode)) {
                for (UserAccount ua : userAccounts) {
                    if (tenantCode.equals(ua.getTenantCode())) {
                        userAccount = ua;
                        break;
                    }
                }
                //00005 = 登陆失败，该租户下不存在此账号！
                if (Objects.isNull(userAccount)) {
                    sessionUser.setLoginStatus(LoginStatus.failure);
                    return sessionUser;
                }
            } else {
                //租户代码为空
                return sessionUser;
            }
        } else {
            userAccount = userAccounts.get(0);
        }

        /*
            检查是否冻结
            1.检查租户是否冻结。租户冻结，则下属用户都将不能登录
            2.检查用户是否冻结。冻结则不允许当前用户登录
         */
        //step1.检查租户是否冻结
        LOGGER.debug("check teantcod");
        if (StringUtils.isBlank(tenantCode)) {
            tenantCode = userAccount.getTenantCode();
        }
        boolean isFrozen = tenantService.isFrozen(tenantCode);
        if (isFrozen) {
            sessionUser.setLoginStatus(LoginStatus.frozen);
            return sessionUser;
        } else {
            User user = userAccount.getUser();
            if (Objects.isNull(user)) {
                return sessionUser;
            }
            //step2.检查用户是否被冻结
            isFrozen = user.getFrozen();
            if (isFrozen) {
                sessionUser.setLoginStatus(LoginStatus.frozen);
                return sessionUser;
            }

            password = DigestUtils.md5Hex(password + userAccount.getSalt());
            if (StringUtils.equals(userAccount.getPassword(), password)) {
                //00001 = 登陆成功！
                // 先清理用户权限缓存(退出后才清除权限缓存)
                //userService.clearUserAuthorizedCaches(user.getId());

                sessionUser.setUserId(user.getId());
                sessionUser.setAccount(userAccount.getAccount());
                sessionUser.setTenantCode(userAccount.getTenantCode());

                sessionUser.setUserName(user.getUserName());
                sessionUser.setUserType(user.getUserType());
                sessionUser.setAuthorityPolicy(user.getUserAuthorityPolicy());
                sessionUser.setLoginStatus(LoginStatus.success);

                sessionUser.setLoginTime(new Date());
                sessionUser.setAppId(appId);
                //获取accessToken
                String accessToken = ContextUtil.generateToken(sessionUser);
                LOGGER.debug("after get accessToken ,accessToken is {}", accessToken);
                if (StringUtils.isNotBlank(accessToken)) {
                    sessionUser.setAccessToken(accessToken);
                    return sessionUser;
                } else {
                    LOGGER.error("登录认证失败：accessToken为空。 -> {}", sessionUser);
                    sessionUser.setLoginStatus(LoginStatus.failure);
                    return sessionUser;
                }
            } else {
                //00003 = 登陆失败，密码不正确！
                return sessionUser;
            }
        }
    }

    /**
     * 用户退出登录
     * {@linkplain @Oneway   只有输入参数，无返回值的JAX-WS单向操作}
     * {@linkplain @IgnoreCheckSession 在API方法上忽略session检查}
     *
     * @param userId 用户Id
     */
    @Override
    //@Oneway
    public Boolean logout(String userId) {
        //清除用户权限缓存
        userService.clearUserAuthorizedCaches(userId);
        // 异步加载用户有权限的菜单
        CompletableFuture.runAsync(() -> {
            List<MenuVo> menuVos = userService.getUserAuthorizedMenus(userId);
            if (Objects.isNull(menuVos)) {
                LogUtil.error("userId:[" + userId + "]加载用户有权限的菜单失败！");
            }
        });
        return true;
    }
}
