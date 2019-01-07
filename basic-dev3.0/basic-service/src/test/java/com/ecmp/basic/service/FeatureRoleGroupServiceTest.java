package com.ecmp.basic.service;

import com.ecmp.basic.api.IFeatureRoleGroupService;
import com.ecmp.basic.entity.FeatureRoleGroup;
import com.ecmp.vo.OperateResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 14:08      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class FeatureRoleGroupServiceTest extends BaseContextTestCase {
    @Autowired
    private IFeatureRoleGroupService service;

    @Test
    public void findAll(){
        List<FeatureRoleGroup> roleGroups = service.findAll();
        Assert.assertNotNull(roleGroups);
        roleGroups.forEach((g)->System.out.println(g.getCode()+":"+g.getName()));
    }

    @Test
    public void delete(){
        String id = "E86DFEFE-36B6-11E7-833F-5E6C1619CBC3";
        OperateResult result = service.delete(id);
        System.out.println(result.toString());
        Assert.assertTrue(result.successful());
    }

    @Test
    public void hashSetTest(){
        Set<FeatureRoleGroup> result = new HashSet<>();
        Set<FeatureRoleGroup> set1 = new HashSet<>();
        FeatureRoleGroup entity1 = new FeatureRoleGroup();
        entity1.setId("1");
        entity1.setName("实体1");
        set1.add(entity1);
        FeatureRoleGroup entity2 = new FeatureRoleGroup();
        entity2.setId("3");
        entity2.setName("实体3");
        set1.add(entity2);
        FeatureRoleGroup entity3 = new FeatureRoleGroup();
        entity3.setId("5");
        entity3.setName("实体5");
        set1.add(entity3);
        Set<FeatureRoleGroup> set2 = new HashSet<>();
        FeatureRoleGroup entity4 = new FeatureRoleGroup();
        entity4.setId("1");
        entity4.setName("实体1");
        set2.add(entity4);
        FeatureRoleGroup entity5 = new FeatureRoleGroup();
        entity5.setId("2");
        entity5.setName("实体2");
        set2.add(entity5);
        FeatureRoleGroup entity6 = new FeatureRoleGroup();
        entity6.setId("3");
        entity6.setName("实体3");
        set2.add(entity6);

        //交集
        result.clear();
        result.addAll(set1);
        result.retainAll(set2);
        System.out.println("交集:");
        result.forEach((g)->System.out.println(g.getId()+":"+g.getName()));

        //差集
        result.clear();
        result.addAll(set1);
        result.removeAll(set2);
        System.out.println("差集:");
        result.forEach((g)->System.out.println(g.getId()+":"+g.getName()));

        //并集
        result.clear();
        result.addAll(set1);
        result.addAll(set2);
        System.out.println("并集:");
        result.forEach((g)->System.out.println(g.getId()+":"+g.getName()));
    }

}