package com.ecmp.basic.service;

import com.ecmp.basic.api.IDataRoleAuthTypeValueService;
import com.ecmp.basic.entity.vo.DataRoleRelation;
import com.ecmp.config.util.ApiClient;
import com.ecmp.core.entity.auth.AuthEntityData;
import com.ecmp.core.entity.auth.AuthTreeEntityData;
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
 * 1.0.00      2017-06-01 10:36      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DataRoleAuthTypeValueServiceTest extends BaseContextTestCase {
    @Test
    public void getUnassignedAuthTreeDataList() throws Exception {
        String roleId = "eb7eb054-4672-11e7-8cd2-005056930c6b";
        String authTypeId = "F38C2DEE-4F44-11E7-B6C2-960F8309DEA7";
        List<AuthTreeEntityData> result = service.getUnassignedAuthTreeDataList(roleId,authTypeId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getUnassignedAuthTreeDataListVaiApi() throws Exception {
        String roleId = "eb7eb054-4672-11e7-8cd2-005056930c6b";
        String authTypeId = "F38C2DEE-4F44-11E7-B6C2-960F8309DEA7";
        IDataRoleAuthTypeValueService proxy = ApiClient.createProxy(IDataRoleAuthTypeValueService.class);
        List<AuthTreeEntityData> result = proxy.getUnassignedAuthTreeDataList(roleId,authTypeId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getAssignedAuthTreeDataList() throws Exception {
        String roleId = "eb7eb054-4672-11e7-8cd2-005056930c6b";
        String authTypeId = "F38C2DEE-4F44-11E7-B6C2-960F8309DEA7";
        List<AuthTreeEntityData> result = service.getAssignedAuthTreeDataList(roleId,authTypeId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getAssignedAuthDataList() throws Exception {
        String roleId = "eb7eb054-4672-11e7-8cd2-005056930c6b";
        String authTypeId = "ab97bc91-4670-11e7-8cd2-005056930c6b";
        List<AuthEntityData> result = service.getAssignedAuthDataList(roleId,authTypeId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getUnAssignedAuthDataList() throws Exception {
        String roleId = "F06A52FD-A767-11E8-A879-0242C0A84409";
        String authTypeId = "1CF53052-A76B-11E8-A879-0242C0A84409";
        List<AuthEntityData> result = service.getUnassignedAuthDataList(roleId, authTypeId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void removeRelations() throws Exception {
        DataRoleRelation relation = new DataRoleRelation();
        relation.setDataRoleId("7509248F-CAC9-11E7-AA54-0242C0A84202");
        relation.setDataAuthorizeTypeId("72DEF84A-A322-11E7-A967-02420B99179E");
        List<String> entityIds = new ArrayList<>();
        entityIds.add("9BAE644A-DE40-11E7-98D7-0242C0A84202");
        relation.setEntityIds(entityIds);
        OperateResult result = service.removeRelations(relation);
        Assert.assertTrue(result.successful());
        System.out.println(result);
    }

    @Test
    public void insertRelations() throws Exception {
        DataRoleRelation relation = new DataRoleRelation();
        relation.setDataRoleId("eb7eb054-4672-11e7-8cd2-005056930c6b");
        relation.setDataAuthorizeTypeId("ab97bc91-4670-11e7-8cd2-005056930c6b");
        List<String> entityIds = new ArrayList<>();
        entityIds.add("c0a80171-5bcd-1066-815b-cd868cd80007");
        entityIds.add("29961710-410A-11E7-91BA-ACE010C46AFD");
        relation.setEntityIds(entityIds);
        OperateResult result = service.insertRelations(relation);
        Assert.assertTrue(result.successful());
        System.out.println(result);
    }

    @Autowired
    private DataRoleAuthTypeValueService service;
}