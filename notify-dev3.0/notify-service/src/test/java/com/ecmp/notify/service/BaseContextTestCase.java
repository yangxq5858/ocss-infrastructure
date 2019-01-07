package com.ecmp.notify.service;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.service.MonitorService;
import com.ecmp.log.util.LogUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.ResponseData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * *************************************************************************************************
 * <p>
 * 实现功能：
 * Spring的支持依赖注入的JUnit4 集成测试基类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/2/8 10:20      马超(Vision)                新建
 * <p>
 * *************************************************************************************************
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseContextTestCase extends AbstractJUnit4SpringContextTests {
    @Before
    public void setUp() {
        LogUtil.debug(ContextUtil.mockUser().toString());
    }
}
