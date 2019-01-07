package com.ecmp.basic.service;

import com.ecmp.basic.api.IExpertUserService;
import com.ecmp.basic.entity.ExpertUser;
import com.ecmp.config.util.ApiClient;
import com.ecmp.vo.OperateResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * Author:jamson
 * date:2018/3/16
 */
public class ExpertUserServiceTest extends BaseContextTestCase {
    @Autowired
    private ExpertUserService expertUserService;

    @Test
    public void testFreeze() {
        String expertId = "";
        OperateResult operateResult = expertUserService.freezeByExpertId(expertId, true);
        Assert.assertTrue(operateResult.successful());
    }

    @Test
    public void testFindOne() {
        String id = "4D267ADC-30C8-11E8-873D-0242C0A84202";
        ExpertUser expertUser = ApiClient.createProxy(IExpertUserService.class).findOne(id);
        Assert.assertTrue(Objects.nonNull(expertUser));
    }
}
