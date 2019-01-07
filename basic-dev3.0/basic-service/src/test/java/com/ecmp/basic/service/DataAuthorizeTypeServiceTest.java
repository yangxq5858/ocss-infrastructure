package com.ecmp.basic.service;

import com.ecmp.basic.entity.vo.DataAuthorizeTypeVo;
import com.ecmp.util.JsonUtils;
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
 * 1.0.00      2017-06-01 14:34      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DataAuthorizeTypeServiceTest extends BaseContextTestCase {
    @Test
    public void getByDataRole() throws Exception {
        String roleId = "eb7eb054-4672-11e7-8cd2-005056930c6b";
        List<DataAuthorizeTypeVo> result = service.getByDataRole(roleId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }

    @Autowired
    private DataAuthorizeTypeService service;



    @Test
    public void getByAppModuleAndDataRole() throws Exception {
        String appModuleId = "68bbd590-35ef-11e7-abce-005056930c6b";
        String roleId = "eb7eb054-4672-11e7-8cd2-005056930c6b";
        List<DataAuthorizeTypeVo> result = service.getByAppModuleAndDataRole(appModuleId,roleId);
        Assert.assertNotNull(result);
        //输出
        System.out.println(JsonUtils.toJson(result));
    }



}