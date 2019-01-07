package com.ecmp.basic.service;

import com.ecmp.basic.api.IOrganizationService;
import com.ecmp.basic.entity.Organization;
import com.ecmp.basic.entity.vo.OrganizationDimension;
import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.ApiJsonUtils;
import com.ecmp.core.entity.auth.AuthTreeEntityData;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：组织机构服务测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 16:25        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
public class OrganizationServiceTest extends BaseContextTestCase {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void testSave(){
        Organization org = new Organization();
        org.setTenantCode("1002");
        org.setCode("100122");
        org.setName("销售部");
        org.setRank(2);
        org.setParentId("141414cd-5b9d-1cbd-815b-9dccd8110000");

        OperateResultWithData<Organization> resultWithData = organizationService.save(org);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 测试：通过租户代码获取组织机构根节点
     */
    @Test
    public void testFindRootByTenantCode(){
        Organization result = organizationService.findRootByTenantCode("10027");
        Assert.assertNotNull(result);
    }

    /**
     * 测试：通过租户代码获取组织机构树
     */
    @Test
    public void testFindOrgTreeByTenantCode(){
        IOrganizationService proxy = ApiClient.createProxy(IOrganizationService.class);
        //Organization result = organizationService.findOrgTree();
        Organization result = proxy.findOrgTree();
        Assert.assertNotNull(result);
    }

    /**
     * 测试：通过租户代码获取组织机构树
     */
    @Test
    public void testMove(){
        String nodeId = "141414cd-5b9e-10ea-815b-9efb03470002";
//        String targetParentId = "7f000001-5ba2-1a45-815b-a7da43800039";
        String targetParentId = "141414cd-5b9d-1cbd-815b-9dccd8110000";
        OperateResult result = organizationService.move(nodeId, targetParentId);
        Assert.assertTrue(result.successful());
    }

    @Test
    public void testMoveViaApi(){
        String nodeId = "141414cd-5b9d-1ee9-815b-9ddf05c60000";
        String targetParentId = "141414cd-5b9d-1de9-815b-9dce05050000";
        IOrganizationService proxy = ApiClient.createProxy(IOrganizationService.class);
        OperateResult result = proxy.move(nodeId, targetParentId);
        Assert.assertTrue(result.successful());
    }

    @Test
    public void testFindOrgTreeByTenantCodeViaApi(){
        IOrganizationService proxy = ApiClient.createProxy(IOrganizationService.class);
        Organization result = proxy.findOrgTree();
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne(){
        String organizationId = "00856890-A330-11E7-A967-02420B99179E";
        Organization result = organizationService.findOne(organizationId);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOrgTreeWithoutFrozen(){
        //String organizationId = "c0a80171-5bcd-1066-815b-cd8371570001";
        List<Organization> result = organizationService.findOrgTreeWithoutFrozen();
        Assert.assertNotNull(result);
    }

    @Test
    public void getAuthTreeEntityDataByIds(){
        List<String> ids = new ArrayList<>();
        ids.add("c0a80171-5bcd-1066-815b-cd83f5a20002");
        ids.add("c0a80171-5bcd-1066-815b-cd8415680003");
        ids.add("C2BEB646-3B9A-11E7-8985-9681B6E77C6A");
        ids.add("c0a80171-5bcd-1066-815b-cd8371570001");
        List<AuthTreeEntityData> result = organizationService.getAuthTreeEntityDataByIds(ids);
        Assert.assertNotNull(result);
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void findAllAuthTreeEntityData(){
        List<AuthTreeEntityData> result = organizationService.findAllAuthTreeEntityData();
        Assert.assertNotNull(result);
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getTree4Unfrozen() {
        String id = "F99F8B2E-5D57-11E7-B670-024288FEC094";
        IOrganizationService proxy = ApiClient.createProxy(IOrganizationService.class);
        Organization organization = proxy.getTree4Unfrozen(id);
        System.out.println(ApiJsonUtils.toJson(organization));
    }

    @Test
    public void getChildrenNodes4Unfrozen() {
        String id = "00856890-A330-11E7-A967-02420B99179E";
        IOrganizationService proxy = ApiClient.createProxy(IOrganizationService.class);
        List<Organization> list = proxy.getChildrenNodes4Unfrozen(id);
        Assert.assertNotNull(list);
    }

    @Test
    public void getUserAuthorizedTreeEntities(){
        List<Organization> organizations = organizationService.getUserAuthorizedTreeEntities(null);
        Assert.assertNotNull(organizations);
        System.out.println(ApiJsonUtils.toJson(organizations));
    }

    @Test
    public void clearBrmOrgCaches(){
        String pattern = "BrmOrgParentCodes_*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys!=null&&!keys.isEmpty()){
            redisTemplate.delete(keys);
        }
    }

    @Test
    public void clearOrgOtherCaches() {
        organizationService.clearOrgOtherCaches();
    }

    @Test
    public void findOrganizationDimension(){
        List<OrganizationDimension> organizations = organizationService.findOrganizationDimension();
        Assert.assertNotNull(organizations);
        System.out.println(ApiJsonUtils.toJson(organizations));
    }
}
