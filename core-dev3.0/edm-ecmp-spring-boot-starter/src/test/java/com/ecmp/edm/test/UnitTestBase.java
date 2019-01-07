package com.ecmp.edm.test;

import com.ecmp.context.ContextUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-11 17:10      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTestBase {

    @Before
    public void initTest() throws Exception {
        ContextUtil.mockUser();
        System.out.println(ContextUtil.getTenantCode() + "-" + ContextUtil.getUserAccount());
    }
}
