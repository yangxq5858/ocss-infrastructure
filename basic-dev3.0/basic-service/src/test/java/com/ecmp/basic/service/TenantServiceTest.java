package com.ecmp.basic.service;

import com.ecmp.basic.api.ITenantService;
import com.ecmp.basic.entity.Tenant;
import com.ecmp.config.util.ApiClient;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：租户service层测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 9:22            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
public class TenantServiceTest extends BaseContextTestCase {
    @Autowired
    private TenantService tenantService;

    @Test
    public void testSave() {
        Tenant tenant = new Tenant();
        tenant.setName("test1");
        OperateResultWithData<Tenant> resultWithData = tenantService.save(tenant);
        Assert.assertTrue(resultWithData.successful());
    }

    @Test
    public void testSaveViaApi() {
        ITenantService proxy = ApiClient.createProxy(ITenantService.class);
//        ITenantService proxy = ApiLocalClient.createProxy(ITenantService.class);
        Tenant tenant = new Tenant();
        tenant.setName("tenantTest2");
        tenant.setCode("121");
        OperateResultWithData<Tenant> resultWithData = proxy.save(tenant);
        Assert.assertTrue(resultWithData.successful());
    }

    @Test
    public void testFindAll() {
        List<Tenant> tenantList = tenantService.findAll();
        System.out.println(tenantList.size());
    }

    @Test
    public void testFindAllVidApi() {
        ITenantService proxy = ApiClient.createProxy(ITenantService.class);
        List<Tenant> tenantList = proxy.findAll();
        Assert.assertNotNull(tenantList);
    }


    @Test
    public void testFindOne() {
        Tenant tenant = tenantService.findOne("141414ce-5b84-1c30-815b-843c45830001");
        System.out.println(tenant.getName());
        Assert.assertNotNull(tenant);
    }

    @Test
    public void testFindOneViaApi() {
        ITenantService proxy = ApiClient.createProxy(ITenantService.class);
        Tenant tenant = proxy.findOne("c0a80167-5b7f-1859-815b-7f8867fd0000");
        Assert.assertNotNull(tenant);
    }

    @Test
    public void testFrozen() {
        boolean frozen = tenantService.isFrozen("10036");
        Assert.assertFalse(frozen);
    }
}
