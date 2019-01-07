package com.ecmp.context;

import com.ecmp.log.util.LogUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/14 11:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContextUtilTest {

    @Test
    public void getGlobalPropertyTest() {
        String templateCode = "EMAIL_TEMPLATE_REGIST";
        //获取模板内容
        String template = ContextUtil.getGlobalProperty(templateCode);
        System.out.println(template);

    }

    @Test
    public void getGlobalPropertiesTest() {
        String templateCode = "ECMP_EMAIL";
        //
        Properties template = ContextUtil.getGlobalProperties(templateCode);
        System.out.println(template);

    }

    @Test
    public void getMessage() {
        LogUtil.debug(ContextUtil.getMessage("ecmp_context_00001"));
        LogUtil.debug(ContextUtil.getMessage("ecmp_context_00002"));

    }
}