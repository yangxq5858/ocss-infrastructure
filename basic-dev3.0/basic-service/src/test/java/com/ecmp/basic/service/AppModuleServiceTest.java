package com.ecmp.basic.service;

import com.ecmp.basic.api.IAppModuleService;
import com.ecmp.basic.entity.AppModule;
import com.ecmp.config.util.ApiClient;
import com.ecmp.context.ContextUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import com.ecmp.vo.SessionUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * 应用模块Entity定义
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00     2017/4/19  19:46    余思豆(yusidou)                 新建
 * <p/>
 * *************************************************************************************************
 */
public class AppModuleServiceTest extends BaseContextTestCase {
    @Autowired
    private IAppModuleService appModuleService;

    /**
     * 测试查询所有
     */
    @Test
    public void findAll() {
        List<AppModule> moduleList = appModuleService.findAll();
        System.out.println(moduleList.size());
    }


//    @Test
//    public void testToken() {
//        String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjoib2JnZndrIiwidXNlcl9uYW1lIjoiYWRtaW4iLCJhdXRoUG9saWN5IjoiVGVuYW50QWRtaW4iLCJyZXNwb25zZV90eXBlIjoiY29kZSIsInRlbmFudENvZGUiOiIxMDAxMSIsInVzZXJOYW1lIjoi5bCP55m9IiwidXNlcklkIjoiREMwRjY2OUEtM0JCNy0xMUU3LUEzNEItQUNFMDEwQzQ2QUZEIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImNsaWVudF9pZCI6ImZsb3ciLCJhdWQiOlsiRUNNUC1BcHBsaWNhdGlvbiJdLCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sInN0YXJ0VGltZSI6MTUwODIxOTcxOTk0MiwicmVkaXJlY3RfdXJpIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3dlYiIsInVzZXJUeXBlIjoiMCIsImV4cCI6MTUwODIyMzMxOSwianRpIjoiYmU0OGQ1ZGQtMzJjZS00NzU4LWEzZmEtNDMzM2I5Y2M4ZTE5IiwiYWNjb3VudCI6ImFkbWluIn0.ZVxsEFj3-8wEnMNLqEVMzTI1m4yaJuE94XISZ7CtzcrX99PK_-gnVK_D6cvImZC8JCCfQpJulRgWWxj5yT980PbbT80jLxvF7gYBJu5cg87CY11S8KiO0awsStfpkGorFB4w-lAIsnGzc0CHD4bMnzEpIPqWDb3ZsW1Y30BTqkONmHiRNHTeAEbYXFOqQ2FxKi15sF9yR4tMkJmtB5dfBu9eV3B7o1ZQE_ZwSPjmngDjc5_Xg2QOVEuYfXhTIQCeg5-gF3hx-VQPpT_H9yLgVeeo2UfDfu0PzMdXvaAfV9sDG6c3T2pbSXGfN3fricBdntFwNGEUhp2YVvgRa9zZFw";
//
//        try {
//            JwtContext context = ContextUtil.getJwtContext(accessToken);
//
//            SessionUser sessionUser = ContextUtil.getSessionUser(context);
//            System.out.println(sessionUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 用API测试查询所有
     */
    @Test
    public void findAllViaApi() {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setAccessToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiRUNNUC1BcHBsaWNhdGlvbiJdLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJzdGFydFRpbWUiOjE1MDgyMDkzMzc5MjMsImV4cCI6MTUwODIxMjkzNywiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImQ1ZmM0Nzg3LTUzYWUtNDQ2OC04MmY4LTdkMDNmZTFhMmViNiIsImNsaWVudF9pZCI6ImZsb3cifQ.X748tGn2Jb3dfZ8A1WXctaGQLjvkAzNmb6VMC2432LiIFZUKaB3oGpaP_GaaTF6rDvRnionVN4QWzyNFFf2i0Bu68PVrxpfuo1SyTNc9zdi3bBIV9QeHejfSj5UR5SB4D17tstOo0qkL-lYcCBoIbmh20z23hn-RbL_PCLs3unGbU-vT_YMTaZ0lX_G-KhJKPGC-yjRYSsUJJRiC4pOUFsV3rUsFSHU8X8gTz3njUrBpmFi_GsHGhBBLLZQ_ZQrCSk69dEI8Ers1Y8HAyV2QjtW6aQpHeCS9De8re179z2Cz5EHktV69Zx-lDRJQqHLb0iYEO6ZQScqQrKwcL9mEng");
        ContextUtil.setSessionUser(sessionUser);
        IAppModuleService proxy = ApiClient.createProxy(IAppModuleService.class);
        List<AppModule> moduleList = proxy.findAll();
        Assert.assertNotNull(moduleList);
        System.out.println("大小：" + moduleList.size());
        System.out.println(JsonUtils.toJson(moduleList));
    }

    /**
     * 保存一个应用模块实体
     */
    @Test
    public void testSave() {
        OperateResultWithData resultWithData = null;
        for (int i = 8; i < 9; i++) {
            AppModule appModule = new AppModule();
            appModule.setName("app" + i);
            appModule.setCode("code" + i);
            appModule.setRank(1);
            resultWithData = appModuleService.save(appModule);
        }
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 用代理的方式保存一个应用模块实体
     */
    @Test
    public void testSaveApi() {
        IAppModuleService proxy = ApiClient.createProxy(IAppModuleService.class);
        AppModule appModule = new AppModule();
        appModule.setName("app2");
        appModule.setCode("code2");
        appModule.setRank(2);
        OperateResultWithData<AppModule> resultWithData = proxy.save(appModule);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 删除一个应用模块实体
     */
    @Test
    public void testDelete() {
        OperateResult result = appModuleService.delete("5250A28E-3627-11E7-B373-A86BAD058F5B");
        System.out.println(result.getMessage());
        Assert.assertFalse(result.successful());
    }

    /**
     * 用代理的方式删除一个应用模块
     */
    @Test
    public void testDeleteApi() {
        IAppModuleService proxy = ApiClient.createProxy(IAppModuleService.class);
        OperateResult result = proxy.delete("CEFCD6D1-41B2-11E7-BAE4-00FF10F920EB");
        System.out.println(result.getMessage());
        Assert.assertFalse(result.successful());
    }

    /**
     * 通过id查询一个应用模块
     */
    @Test
    public void testFindById() {
        AppModule appModule = appModuleService.findOne("45677D40-3616-11E7-92E5-A86BAD058F5B");
        Assert.assertNotNull(appModule);
    }

    /**
     * 用代理的方式根据id查询一个应用模块
     */
    @Test
    public void testFindByIdViaApi() {
        IAppModuleService proxy = ApiClient.createProxy(IAppModuleService.class);
        AppModule result = proxy.findOne("538D8F8D-3627-11E7-BF30-5E6C1619CBC3");
        Assert.assertNotNull(result);
    }

//
//    /**
//     * 通过sequence查询应用模块清单
//     */
//    @Test
//    public void testFindByRank(){
//        List<AppModule> appModule = appModuleService.findByRank(1);
//        Assert.assertNotNull(appModule);
//    }
//
//    /**
//     * 用代理的方式根据sequence查询应用模块清单
//     */
//    @Test
//    public void testFindByRankViaApi(){
//        IAppModuleService proxy = ApiClient.createProxy(IAppModuleService.class);
//        List<AppModule> appModule = proxy.findByRank(2);
//        Assert.assertNotNull(appModule);
//    }


}
