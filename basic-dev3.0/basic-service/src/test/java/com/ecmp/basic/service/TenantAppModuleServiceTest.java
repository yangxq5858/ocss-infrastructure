package com.ecmp.basic.service;

import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-19 12:08      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class TenantAppModuleServiceTest extends BaseContextTestCase {
    @Test
    public void getAppModuleCodes() throws Exception {
        List<String> codes = service.getAppModuleCodes();
        Assert.assertNotNull(codes);
        //输出
        System.out.println(JsonUtils.toJson(codes));
    }

    @Autowired
    private TenantAppModuleService service;
    @Test
    public void saveRelations() throws Exception {
        String tenantId = "CC339A40-570B-11E7-9C46-024288FEC094";
        List<String> appModuleIds = new ArrayList<>();
        appModuleIds.add("314B7696-36E1-11E7-8427-5E6C1619CBC3");
        appModuleIds.add("68bbd590-35ef-11e7-abce-005056930c6b");
        appModuleIds.add("55B6E377-36E1-11E7-8427-5E6C1619CBC3");
        appModuleIds.add("6AB30CE8-36E1-11E7-8427-5E6C1619CBC3");
        appModuleIds.add("7B8BCB69-36E1-11E7-8427-5E6C1619CBC3");
        appModuleIds.add("64FDCB79-3A9D-11E7-BF9A-9681B6E77C6A");
        appModuleIds.add("CEFCD6D1-41B2-11E7-BAE4-00FF10F920EB");
        appModuleIds.add("95F1C05D-6DBB-11E7-979B-02420B99179E");
        OperateResult result = service.saveRelations(tenantId,appModuleIds);
        System.out.println(result);
        Assert.assertTrue(result.successful());
    }

}