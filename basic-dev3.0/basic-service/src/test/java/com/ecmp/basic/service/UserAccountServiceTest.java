package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserAccountService;
import com.ecmp.basic.entity.UserAccount;
import com.ecmp.enums.UserType;
import com.ecmp.basic.entity.vo.UserAccountVo;
import com.ecmp.config.util.ApiClient;
import com.ecmp.context.ContextUtil;
import com.ecmp.context.common.ConfigConstants;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import com.ecmp.vo.SessionUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：用户帐号服务测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 18:36            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
public class UserAccountServiceTest extends BaseContextTestCase {

    @Autowired
    private UserAccountService userAccountService;

    /**
     * 测试添加
     */
    @Test
    public void testSave() {
        UserAccount userAccount = new UserAccount();
        userAccount.setUserName("管理员");
        userAccount.setUserType(UserType.Supplier);

        userAccount.setNickName("昵称");
        userAccount.setAccount("admin2");
        userAccount.setPassword(DigestUtils.md5Hex("123456"));
        userAccount.setTenantCode("1002");
        OperateResultWithData<UserAccount> resultWithData = userAccountService.save(userAccount);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 通过代理的方式测试添加
     */
    @Test
    public void testSaveViaApi() {
        UserAccount userAccount = new UserAccount();
        userAccount.setUserName("管理员");
        userAccount.setUserType(UserType.Supplier);

        userAccount.setNickName("昵称");
        userAccount.setAccount("admin2");
        userAccount.setPassword(DigestUtils.md5Hex("123456"));
        userAccount.setTenantCode("1001");

        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
        OperateResultWithData<UserAccount> resultWithData = proxy.save(userAccount);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 测试加密
     */
    @Test
    public void testViaApi() {
        String pass = DigestUtils.md5Hex(DigestUtils.md5Hex("123456") + "2ec0d3a6-f543-489d-a562-6412a44f4a2c");
        System.out.println(pass);
        Assert.assertEquals("987193569c3e9c9521ce1a46d2e22a45", pass);
    }

    /**
     * 通过代理的方式测试修改
     */
    @Test
    public void testUpdate() {
        UserAccount userAccount = userAccountService.findOne("c0a80166-5b89-1fee-815b-89803aa80000");

        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
        userAccount.setAccount("bb5");
        //    userAccount.setUserName("u11111");
        //    userAccount.setUserType(1);
        userAccount.setTenantCode("11111");
//        OperateResultWithData<UserAccount> resultWithData = proxy.save(userAccount);
        OperateResultWithData<UserAccount> resultWithData = userAccountService.save(userAccount);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 测试查询所有数据
     */
    @Test
    public void testFindAll() {
        List<UserAccount> userAccountList = userAccountService.findAll();
        Assert.assertEquals(4, userAccountList.size());
        //userAccountService.findByProperty("user.userType", UserType.Employee);
    }

    @Test
    public void testFindByUserId() {
        //IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
//        IUserAccountService proxy = ApiLocalClient.createProxy(IUserAccountService.class);
        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
        List<UserAccountVo> userAccountList = proxy.findByUserId("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        Assert.assertEquals(2, userAccountList.size());
    }


    @Test
    public void findByTenantCodeAndUserUserType() {
        //IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
//        IUserAccountService proxy = ApiLocalClient.createProxy(IUserAccountService.class);
        List<UserAccount> userAccountList = userAccountService.findByTenantCodeAndUserUserType("10011", UserType.Supplier);
        Assert.assertEquals(2, userAccountList.size());
    }

    @Test
    public void findByTenantCodeAndUserUserTypeViaApi() {
        //IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
//        IUserAccountService proxy = ApiLocalClient.createProxy(IUserAccountService.class);
        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
        List<UserAccount> userAccountList = proxy.findByTenantCodeAndUserUserType("10011", UserType.Supplier);
        Assert.assertEquals(2, userAccountList.size());
    }

    /**
     * 测试查询一条数据
     */
    @Test
    public void testFindOne() {
        UserAccount userAccount = userAccountService.findOne("7f000001-5b88-18e0-815b-890d0c60001e");
        Assert.assertNotNull(userAccount);
    }

    /**
     * 通过代理的方式测试查询一条数据
     */
    @Test
    public void testFindOneViaApi() {
        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
//        IUserAccountService proxy = ApiLocalClient.createProxy(IUserAccountService.class);
        UserAccount userAccount = proxy.findOne("c0a80166-5b85-1795-815b-8597a8ad0000");
        Assert.assertNotNull(userAccount);
    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() {
        OperateResult result = userAccountService.delete("c0a80167-5b7f-11a7-815b-7f91b7980002");
        Assert.assertTrue(result.successful());
    }

    /**
     * 通过代理的方式测试删除一条数据
     */
    @Test
    public void testDeleteViaApi() {
        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);
//        IUserAccountService proxy = ApiLocalClient.createProxy(IUserAccountService.class);
        OperateResult result = proxy.delete("c0a80166-5b85-1795-815b-8597a8ad0000");
        Assert.assertTrue(result.successful());
    }

    /**
     * 通过代理的方式测试添加
     */
    @Test
    public void testLoginViaApi() {
        IUserAccountService proxy = ApiClient.createProxy(IUserAccountService.class);

        SessionUser resultWithData = proxy.login(ContextUtil.getAppId(),"10032", "123456", DigestUtils.md5Hex("123456"));
        System.out.println(resultWithData);
    }

    /**
     * 通过代理的方式测试添加
     */
    @Test
    public void testLogin() {
        SessionUser resultWithData = userAccountService.login(ContextUtil.getAppId(), "10044", "654321", DigestUtils.md5Hex("123456"));
        System.out.println(resultWithData);
    }

    @Test
    public void testLogin1() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tenantCode", "10011");
        params.put("account", "admin");
        params.put("password", DigestUtils.md5Hex("123456"));
        String path = "userAccount/login";
//        String paramJson = JsonUtils.toJson(params);
        SessionUser sessionUser = ApiClient.postViaProxyReturnResult(ConfigConstants.BASIC_API, path, SessionUser.class, params);
        System.out.println(sessionUser);
    }

    @Test
    public void logout() throws Exception{
        String userId = "1592D012-A330-11E7-A967-02420B99179E";
        userAccountService.logout(userId);
        Thread.sleep(60000L);
    }

    @Test
    public void logoutViaApi1() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "111");
        String path = "userAccount/logout";
        ApiClient.postViaProxyReturnResult(ConfigConstants.BASIC_API, path, void.class, params);
    }
}
