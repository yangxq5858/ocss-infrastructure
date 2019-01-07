package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureGroupService;
import com.ecmp.basic.api.IFeatureService;
import com.ecmp.basic.dao.TenantDao;
import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.FeatureGroup;
import com.ecmp.basic.entity.Tenant;
import com.ecmp.basic.entity.enums.FeatureType;
import com.ecmp.config.util.ApiClient;
import com.ecmp.core.search.PageInfo;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：测试功能项的API服务实现类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/20 9:17              李汶强                  新建
 * <p/>
 * *************************************************************************************************
 */
public class FeatureServiceTest extends BaseContextTestCase {

    @Autowired
    private FeatureService featureService;
    @Autowired
    private IFeatureGroupService featureGroupService;
    @Autowired
    private TenantDao tenantDao;

    @Test
    public void testFindByFiltersApi() {
        List<String> list = new ArrayList<>();
        list.add("BASIC-YHGL-GYSYHGL");
        list.add("BASIC-YHGL-ZJYH-FREEZE");
        Search search = Search.createSearch().addFilter(new SearchFilter("code", list, SearchFilter.Operator.IN));
        List<Feature> features = ApiClient.createProxy(IFeatureService.class).findByFilters(search);
        System.out.println(features.size());
    }

    @Test
    public void getTenantCanUseFeatures() {
        String tenantCode = "10044";
        Tenant tenant = tenantDao.findByFrozenFalseAndCode(tenantCode);
        List<Feature> features = featureService.getTenantCanUseFeatures(tenant);
        Assert.assertNotNull(features);
        System.out.println(JsonUtils.toJson(features));
    }

    /**
     * 测试根据应用模块Id查询功能项
     */
    @Test
    public void testFindByAppModuleId() {
        List<Feature> featureList = featureService.findByAppModuleId("68bbd590-35ef-11e7-abce-005056930c6b");
        System.out.println(featureList.size());
    }

    /**
     * 通过代理的方式测试根据应用模块Id查询功能项
     */
    @Test
    public void testFindByAppModuleIdViaApi() {
        IFeatureService proxy = ApiClient.createProxy(IFeatureService.class);
        List<Feature> featureList = proxy.findByAppModuleId("68bbd590-35ef-11e7-abce-005056930c6b");
        System.out.println(featureList.size());
    }

    /**
     * 用api根据geatureGroupId查询feature清单
     */
    @Test
    public void testFindByFeatureGroupId() {
        List<Feature> featureList = featureService.findByFeatureGroupId("3AB4F466-3A0E-11E7-A26B-9681B6E77C6A");
        System.out.println(featureList.size());
    }

    /**
     * 根据geatureGroupId查询feature清单
     */
    @Test
    public void testFindByFeatureGroupIdViaApi() {
        IFeatureService proxy = ApiClient.createProxy(IFeatureService.class);
        List<Feature> featureList = proxy.findByFeatureGroupId("4E7E6A38-3A0E-11E7-A26B-9681B6E77C6A");
        System.out.println(featureList.size());
    }

    /**
     * 保存一个功能项实体
     */
    @Test
    public void testSave() {
        FeatureGroup featureGroup = featureGroupService.findOne("71D85D6C-362E-11E7-9A0C-C85B76A749D6");
//        Feature feature = new Feature();
        Feature feature = featureService.findOne("C4CA6F76-363F-11E7-9CA1-A86BAD058F5B");
        feature.setCode("fc02");
        feature.setName("fn02");
        feature.setFeatureType(FeatureType.Operate);
        feature.setFeatureGroup(featureGroup);

        OperateResultWithData<Feature> resultWithData = featureService.save(feature);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 通过Api代理保存一个功能项实体
     */
    @Test
    public void testSaveViaApi() {
        Feature feature = new Feature();
        feature.setCode("fc02");
        feature.setName("fn02");
        feature.setFeatureType(FeatureType.Business);
        FeatureGroup featureGroup = featureGroupService.findOne("e3c6c844-35f6-11e7-abce-005056930c6b");
        feature.setFeatureGroup(featureGroup);
        IFeatureService proxy = ApiClient.createProxy(IFeatureService.class);
        OperateResultWithData<Feature> resultWithData = proxy.save(feature);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 根据Id条件来查询一个功能项
     */
    @Test
    public void testFindOne() {
        Feature feature = featureService.findOne("7f000001-5bd2-109e-815b-d24a6ca30006");
        Assert.assertNotNull(feature);
    }

    /**
     * 通过Api代理根据Id条件来查询一个功能项
     */
    @Test
    public void testFindOneViaApi() {
        IFeatureService proxy = ApiClient.createProxy(IFeatureService.class);
        Feature feature = proxy.findOne("7f000001-5bd2-109e-815b-d24a6ca30006");
        Assert.assertNotNull(feature);
    }

    /**
     * 根据id删除一个功能项
     */
    @Test
    public void testDeleteOne() {
        OperateResult operateResult = featureService.delete("A92BBCF9-3EC3-11E7-84F2-960F8309DEA7");
        Assert.assertTrue(operateResult.successful());
    }

    /**
     * 通过Api代理根据id删除一个功能项
     */
    @Test
    public void testDeleteOneViaApi() {
        IFeatureService proxy = ApiClient.createProxy(IFeatureService.class);
        OperateResult operateResult = proxy.delete("2EF1EB9A-3641-11E7-A14C-5E6C1619CBC3");
        Assert.assertTrue(operateResult.successful());
    }

    /**
     * 测试颁布查询
     */
    @Test
    public void testFindByPage() {
        Search search = new Search();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setRows(3);
        search.setPageInfo(pageInfo);
        PageResult<Feature> pageResult = featureService.findByPage(search);
        System.out.println(JsonUtils.toJson(pageResult));
    }

    /**
     * 用API测试颁布查询
     */
    @Test
    public void testFindByPageViaApi() {
        Search search = new Search();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(2);
        pageInfo.setRows(2);
        search.setPageInfo(pageInfo);
        IFeatureService proxy = ApiClient.createProxy(IFeatureService.class);
        PageResult<Feature> pageResult = proxy.findByPage(search);
        System.out.println(JsonUtils.toJson(pageResult));
    }
}
