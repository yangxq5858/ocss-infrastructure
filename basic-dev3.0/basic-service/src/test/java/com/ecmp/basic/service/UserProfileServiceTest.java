package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserProfileService;
import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.UserProfile;
import com.ecmp.basic.entity.vo.PersonalSettingInfo;
import com.ecmp.config.util.ApiClient;
import com.ecmp.notity.entity.UserNotifyInfo;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户配置service测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 10:44            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
public class UserProfileServiceTest extends BaseContextTestCase {
    @Autowired
    private IUserProfileService userProfileService;

    @Test
    public void testSave() {
        UserProfile userProfile = new UserProfile();
        User user = new User();
        user.setId("7f000001-5b7f-138f-815b-7f8bddde0007");
        userProfile.setUser(user);
        OperateResultWithData<UserProfile> resultWithData = userProfileService.save(userProfile);
        Assert.assertTrue(resultWithData.successful());
    }

    @Test
    public void testSaveViaApi() {
        IUserProfileService proxy = ApiClient.createProxy(IUserProfileService.class);
        UserProfile userProfile = new UserProfile();
        User user = new User();
        user.setId("7f000001-5b7f-138f-815b-7f8bddde0007");
        userProfile.setUser(user);
        OperateResultWithData<UserProfile> resultWithData = proxy.save(userProfile);
        Assert.assertTrue(resultWithData.successful());
    }

    @Test
    public void testFindPersonalSettingInfo() {
        PersonalSettingInfo personalSettingInfo = userProfileService.findPersonalSettingInfo("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        Assert.assertNotNull(personalSettingInfo);
    }


    @Test
    public void findNotifyInfoByUserIds() {
        List<String> userIds = new ArrayList<>();
        userIds.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        userIds.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        List<UserNotifyInfo> userNotifyInfos = userProfileService.findNotifyInfoByUserIds(userIds);
        Assert.assertNotNull(userNotifyInfos);
        System.out.println(JsonUtils.toJson(userNotifyInfos));
    }

    @Test
    public void findNotifyInfoByUserIdsViaApi() {
        IUserProfileService proxy = ApiClient.createProxy(IUserProfileService.class);
        List<String> userIds = new ArrayList<>();
        userIds.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        userIds.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        List<UserNotifyInfo> userNotifyInfos = proxy.findNotifyInfoByUserIds(userIds);
        Assert.assertNotNull(userNotifyInfos);
    }

    @Test
    public void findAccountor() {
        String re = userProfileService.findAccountor();
        System.out.print(re);
    }

}
