package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserService;
import com.ecmp.basic.entity.Corporation;
import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.User;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.MenuVo;
import com.ecmp.config.util.ApiClient;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.entity.auth.AuthEntityData;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户服务测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/18 12:27        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
public class UserServiceTest extends BaseContextTestCase {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void getNormalUserAuthorizedEntities() throws Exception {
        String etityClassName = "com.ecmp.brm.baf.entity.BudgetCorporation";
        String featureCode = null;
        String userId = "13E547CA-CAAF-11E7-AA54-0242C0A84202";
        List<String> entityIds = userService.getNormalUserAuthorizedEntities(etityClassName,featureCode,userId);
        Assert.assertNotNull(entityIds);
        System.out.println(JsonUtils.toJson(entityIds));
    }

    @Test
    public void getUserCanAssignAuthDataList() throws Exception {
        String userId = "F06A52FD-A767-11E8-A879-0242C0A84409";
        String dataAuthTypeId = "ab97bc91-4670-11e7-8cd2-005056930c6b";
        List<AuthEntityData> dataList = userService.getUserCanAssignAuthDataList(dataAuthTypeId,userId);
        Assert.assertNotNull(dataList);
        System.out.println(JsonUtils.toJson(dataList));
    }

    @Test
    public void getUserAuthorizedFeatureMaps() throws Exception {
        String userId = "8f9f3a92-3f82-11e7-ac6f-005056930c6b";
        Map<String,Map<String,String>> result = userService.getUserAuthorizedFeatureMaps(userId);
        Assert.assertNotNull(result);
        //打印输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getUserAuthorizedFeatureMapsViaApi() throws Exception {
        String userId = "4044BFAE-413C-11E7-99DA-960F8309DEA7";
        IUserService proxy = ApiClient.createProxy(IUserService.class);
        Map<String,Map<String,String>> menuVos = proxy.getUserAuthorizedFeatureMaps(userId);
        Assert.assertNotNull(menuVos);
        //输出
        System.out.println(JsonUtils.toJson(menuVos));
    }

    @Test
    public void getUserCanAssignFeatures() throws Exception {
        String userId = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        List<Feature> result = userService.getUserCanAssignFeatures(userId);
        Assert.assertNotNull(result);
        //打印输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getUserAuthorizedMenus() throws Exception {
        String userId = ContextUtil.getUserId();
        List<MenuVo> menuVos = userService.getUserAuthorizedMenus(userId);
        Assert.assertNotNull(menuVos);
        //输出
        System.out.println(JsonUtils.toJson(menuVos));
    }

    @Test
    public void getUserAuthorizedMenusViaApi() throws Exception {
        String userId = ContextUtil.getUserId();
        IUserService proxy = ApiClient.createProxy(IUserService.class);
        List<MenuVo> menuVos = proxy.getUserAuthorizedMenus(userId);
        Assert.assertNotNull(menuVos);
        //输出
        System.out.println(JsonUtils.toJson(menuVos));
    }

    @Test
    public void hasFeatureAuthority() throws Exception {
        String userId = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        String featureCode = "BASIC-ZZJG-GWGL";
        boolean passed = userService.hasFeatureAuthority(userId,featureCode);
        Assert.assertTrue(passed);
    }

    @Test
    public void clearUserAuthorizedCaches() throws Exception {
        String userId = "13E547CA-CAAF-11E7-AA54-0242C0A84202";
        userService.clearUserAuthorizedCaches(userId);
    }

    @Test
    public void getUserAuthorizedFeatures() throws Exception {
        String userId = "13E547CA-CAAF-11E7-AA54-0242C0A84202";
        List<Feature> result = userService.getUserAuthorizedFeatures(userId);
        Assert.assertNotNull(result);
        //打印输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void testSave(){
        User user = new User();
        OperateResultWithData<User> resultWithData = userService.save(user);
        Assert.assertTrue(resultWithData.successful());
    }

    @Test
    public void redisTest() {
        //设置值
        String key = "my-test01";
        String value = "test redis";
        redisTemplate.opsForValue().set(key, value);
        //获取值
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        Assert.assertEquals(value, redisValue);
        //获取键
        Set<String> keys = redisTemplate.keys("*test*");
        System.out.println(JsonUtils.toJson(keys));

        // 获取用户数据权限
        String dataKey = "com.ecmp.basic.entity.Corporation_null_13E547CA-CAAF-11E7-AA54-0242C0A84202";
        Object ids = redisTemplate.opsForValue().get(dataKey);
        System.out.println(JsonUtils.toJson(ids));
    }

    @Test
    public void getExecutorsByUserIds() {
        List<String> list =new ArrayList<String>();
        list.add("00A0E06A-CAAF-11E7-AA54-0242C0A84202");
        list.add("3F91BC29-6B93-11E8-B6CC-0A580A8100A6");
        List<Executor> executors = userService.getExecutorsByUserIds(list);
        System.out.println(JsonUtils.toJson(executors));
    }



}
