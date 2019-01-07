package com.ecmp.basic.service;

import com.ecmp.basic.entity.FeatureRole;
import com.ecmp.basic.entity.enums.RoleType;
import com.ecmp.util.EnumUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 16:41      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class FeatureRoleServiceTest extends BaseContextTestCase {
    @Autowired
    private FeatureRoleService service;

    @Test
    public void isExistByFeatureRoleGroup() throws Exception {
        String roleGroupId = "E86DFEFE-36B6-11E7-833F-5E6C1619CBC3";
        Boolean role = service.isExistsByProperty("featureRoleGroup.id",roleGroupId);
        Assert.assertTrue(role);
    }

    @Test
    public void findByFeatureRoleGroup() throws Exception {
        String roleGroupId = "d25ba27b-30a7-11e7-abce-005056930c6b";
        List<FeatureRole> roles = service.findByFeatureRoleGroup(roleGroupId);
        Assert.assertNotNull(roles);
        roles.forEach((r)->{
            System.out.println(r.getCode()+":"+r.getName());
            if (r.getRoleType()!=null){
                System.out.println("角色类型："+ EnumUtils.getEnumItemRemark(RoleType.class,r.getRoleType().ordinal()));
            }
        });
    }

}