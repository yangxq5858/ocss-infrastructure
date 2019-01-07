package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserProfileService;
import com.ecmp.basic.dao.EmployeeDao;
import com.ecmp.basic.dao.UserProfileDao;
import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.UserProfile;
import com.ecmp.enums.UserType;
import com.ecmp.basic.entity.vo.PersonalSettingInfo;
import com.ecmp.config.util.IgnoreCheckSession;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.notity.entity.UserNotifyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现用户配置的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/14 15:45            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class UserProfileService extends BaseEntityService<UserProfile> implements IUserProfileService {
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private EmployeeDao employeeDao;

    @Override
    protected BaseEntityDao<UserProfile> getDao() {
        return userProfileDao;
    }

    /**
     * 根据用户的id查询用户配置
     *
     * @param userId 用户id
     * @return 用户配置
     */
    @Override
    public PersonalSettingInfo findPersonalSettingInfo(String userId) {
        UserProfile userProfile = userProfileDao.findByUserId(userId);
        PersonalSettingInfo personalSettingInfo = new PersonalSettingInfo(userProfile);
        UserType userType = userProfile.getUser().getUserType();
        if (userType == UserType.Employee) {
            Employee employee = employeeDao.findOne(userId);
            if (employee != null) {
                personalSettingInfo.setCode(employee.getCode());
                personalSettingInfo.setOrganizationName(employee.getOrganization().getNamePath().substring(1));
            }
        } else {
            //合作伙伴
        }
        return personalSettingInfo;
    }

    /**
     * 根据用户id列表获取通知信息
     *
     * @param userIds 用户id集合
     */
    @Override
    @IgnoreCheckSession
    public List<UserNotifyInfo> findNotifyInfoByUserIds(List<String> userIds) {
        List<UserProfile> userProfiles = userProfileDao.findNotifyInfoByUserIds(userIds);
        List<UserNotifyInfo> userNotifyInfos = new ArrayList<>();
        userProfiles.forEach((r) -> {
            UserNotifyInfo userNotifyInfo = new UserNotifyInfo();
            userNotifyInfo.setUserId(r.getUser().getId());
            userNotifyInfo.setUserName(r.getUser().getUserName());
            userNotifyInfo.setEmail(r.getEmail());
            userNotifyInfo.setMobile(r.getMobile());
            userNotifyInfos.add(userNotifyInfo);
        });
        return userNotifyInfos;
    }

    /**
     * 查询一个用户配置
     *
     * @param userId 用户id
     * @return 用户配置
     */
    @Override
    public UserProfile findByUserId(String userId) {
        return userProfileDao.findByUserId(userId);
    }

    /**
     * 获取当前用户的记账用户
     *
     * @return 记账用户
     */
    @Override
    public String findAccountor() {
        UserProfile userProfile = findByUserId(ContextUtil.getUserId());
        if (Objects.nonNull(userProfile)) {
            return userProfile.getAccountor();
        }
        return null;
    }
}

