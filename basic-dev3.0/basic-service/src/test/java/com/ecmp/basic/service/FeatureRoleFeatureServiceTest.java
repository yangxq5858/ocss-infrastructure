package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureRoleFeatureService;
import com.ecmp.basic.entity.Feature;
import com.ecmp.config.util.ApiClient;
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
 * 1.0.00      2017-05-05 10:01      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class FeatureRoleFeatureServiceTest extends BaseContextTestCase {
    @Test
    public void getUnassignedChildren() throws Exception {
        String parentId = "81b5c647-30a9-11e7-abce-005056930c6b";
        List<Feature> features = service.getUnassignedChildren(parentId);
        Assert.assertNotNull(features);
        System.out.println("未分配："+features.size());
    }

    @Test
    public void removeRelations() throws Exception {
        String parentId = "81b5c647-30a9-11e7-abce-005056930c6b";
        List<String> childIds = new ArrayList<>();
        childIds.add("7f000001-5bd2-109e-815b-d24b9dcd0007");
        childIds.add("97F6E5BB-3A0F-11E7-A26B-9681B6E77C6A");
        OperateResult result = service.removeRelations(parentId,childIds);
        Assert.assertTrue(result.successful());
        //打印操作结果
        System.out.println(JsonUtils.toJson(result));
    }

    @Autowired
    private IFeatureRoleFeatureService service;

    @Test
    public void insertRelations() throws Exception {
        String parentId = "81b5c647-30a9-11e7-abce-005056930c6b";
        List<String> childIds = new ArrayList<>();
        childIds.add("0731274A-39F2-11E7-AD08-9681B6E77C6A");
        IFeatureRoleFeatureService proxy = ApiClient.createProxy(IFeatureRoleFeatureService.class);
        OperateResult result = proxy.insertRelations(parentId, childIds);
        Assert.assertTrue(result.successful());
        //打印操作结果
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void getChildrenFromParentId() throws Exception {
        String parentId = "81b5c647-30a9-11e7-abce-005056930c6b";
        List<Feature> features = service.getChildrenFromParentId(parentId);
        Assert.assertNotNull(features);
        features.forEach((f)->System.out.println(f.getCode()+":"+f.getName()));
    }

}