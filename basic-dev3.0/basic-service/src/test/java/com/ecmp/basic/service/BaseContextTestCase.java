package com.ecmp.basic.service;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.service.MonitorService;
import com.ecmp.log.util.LogUtil;
import com.ecmp.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：单元测试的基类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 20:27      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseContextTestCase extends AbstractJUnit4SpringContextTests {
    @Autowired
    private MonitorService monitorService;

    @Before
    public void setUp() {
        LogUtil.debug(ContextUtil.mockUser().toString());
    }

    @Test
    public void health(){
//        String[] result = monitorService.health();
//        System.out.println(JsonUtils.toJson(result));
    }
}
