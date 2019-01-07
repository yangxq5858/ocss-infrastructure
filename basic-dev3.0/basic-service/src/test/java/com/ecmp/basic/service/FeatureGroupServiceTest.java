package com.ecmp.basic.service;

import com.ecmp.basic.api.IAppModuleService;
import com.ecmp.basic.api.IFeatureGroupService;
import com.ecmp.basic.dao.FeatureGroupDao;
import com.ecmp.basic.entity.AppModule;
import com.ecmp.basic.entity.FeatureGroup;
import com.ecmp.config.util.ApiClient;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
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
 * 1.0.00     2017/4/20  10:27    余思豆(yusidou)                 新建
 * <p/>
 * *************************************************************************************************
 */
public class FeatureGroupServiceTest extends BaseContextTestCase {

    @Autowired
    private IFeatureGroupService featureGroupService;
    @Autowired
    private IAppModuleService appModuleService;
    @Autowired
    private FeatureGroupDao featureGroupDao;

    /**
     * 测试根据应用模块id查询功能项组
     */
    @Test
    public void testFindByAppModuleId() {
        List<FeatureGroup> featureGroupList = featureGroupService.findByAppModuleId("3935E405-3A05-11E7-BE98-A86BAD058F5B");
        System.out.println(featureGroupList.size());
    }

    /**
     * 能过代理的方式测试根据应用模块id查询功能项组
     */
    @Test
    public void testFindByAppModuleIdViaApi() {
        IFeatureGroupService proxy = ApiClient.createProxy(IFeatureGroupService.class);
        List<FeatureGroup> featureGroupList = proxy.findByAppModuleId("3935E405-3A05-11E7-BE98-A86BAD058F5B");
        System.out.println(featureGroupList.size());
    }

    /**
     * 保存一个功能项组
     */
    @Test
    public void testSave() {
//        AppModule appModule = new AppModule();
//        appModule.setCode("code6");
//        appModule.setName("app3");
//        appModule.setRank(1);
//        appModule.setId(UUID.randomUUID().toString());
//        appModuleService.save(appModule);
        AppModule appModule = appModuleService.findOne("7B8BCB69-36E1-11E7-8427-5E6C1619CBC3");
        FeatureGroup featureGroup = new FeatureGroup();
        featureGroup.setName("testFG");
        featureGroup.setCode("testFG");
        featureGroup.setAppModule(appModule);
        OperateResultWithData<FeatureGroup> resultWithData = featureGroupService.save(featureGroup);
        System.out.println(resultWithData.getMessage());
        ;
    }

    /**
     * 用代理的方式保存一个功能项组
     */
    @Test
    public void testSaveApi() {
//        AppModule appModule = new AppModule();
//        appModule.setCode("code7");
//        appModule.setName("app3");
//        appModule.setRank(1);
//        appModuleService.save(appModule);
        AppModule appModule = appModuleService.findOne("7B8BCB69-36E1-11E7-8427-5E6C1619CBC3");
        FeatureGroup featureGroup = new FeatureGroup();
        featureGroup.setName("testFG");
        featureGroup.setCode("testFG");
        featureGroup.setAppModule(appModule);
        IFeatureGroupService proxy = ApiClient.createProxy(IFeatureGroupService.class);
        OperateResultWithData<FeatureGroup> result = proxy.save(featureGroup);
        Assert.assertTrue(result.successful());
    }

    /**
     * 删除一个功能项
     */
    @Test
    public void testDelete() {
        OperateResult resultWithData = featureGroupService.delete("BACC65CA-3A0E-11E7-A26B-9681B6E77C6A");
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 通过代理的方式删除一个功能项
     */
    @Test
    public void testDeleteApi() {
        IFeatureGroupService proxy = ApiClient.createProxy(IFeatureGroupService.class);
        OperateResult result = proxy.delete("c0a80166-5b8a-15b7-815b-8a15d0040000");
        Assert.assertTrue(result.successful());
    }

    /**
     * 通过id查询功能项
     */
    @Test
    public void testFindOne() {
        FeatureGroup featureGroup = featureGroupService.findOne("DFAD7C28-5A33-11E7-8D14-C85B76A749D6");
        Assert.assertNotNull(featureGroup);
        System.out.println(JsonUtils.toJson(featureGroup));
    }

    /**
     * 用API通过id查询功能项
     */
    @Test
    public void testFindOneViaApi() {
        IFeatureGroupService proxy = ApiClient.createProxy(IFeatureGroupService.class);
        FeatureGroup featureGroup = proxy.findOne("71D85D6C-362E-11E7-9A0C-C85B76A749D6");
        Assert.assertNotNull(featureGroup);
    }

    @Test
    public void testClearCache(){
        featureGroupDao.evict();
    }

    /**
     * 测试查询所有
     */
    @Test
    public void testFindAll() {
        List<FeatureGroup> featureGroups = featureGroupService.findAll();
        System.out.println(featureGroups.size());
    }

    /**
     * 用api测试查询所有
     */
    @Test
    public void testFindAllViaApi() {
        IFeatureGroupService proxy = ApiClient.createProxy(IFeatureGroupService.class);
        List<FeatureGroup> groupList = proxy.findAll();
        System.out.println(groupList.size());
    }

    /**
     * 测试模糊查询
     */
    @Test
    public void testFindByNameLike() {
        List<FeatureGroup> featureGroupList = featureGroupService.findByNameLike("架");
        System.out.println(featureGroupList.size());
    }

    /**
     * 通过代理的方式测试模糊查询
     */
    @Test
    public void testFindByNameLikeViaApi() {
        IFeatureGroupService proxy = ApiClient.createProxy(IFeatureGroupService.class);
        List<FeatureGroup> featureGroupList = proxy.findByNameLike("架");
        System.out.println(featureGroupList.size());
    }

}
