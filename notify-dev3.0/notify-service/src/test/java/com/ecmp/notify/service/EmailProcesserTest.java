package com.ecmp.notify.service;

//import com.ecmp.config.ServiceConfig;
import com.ecmp.context.ContextUtil;
import com.ecmp.notity.entity.EmailAccount;
import com.ecmp.notity.entity.EmailMessage;
import com.ecmp.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-17 10:19      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class EmailProcesserTest extends BaseContextTestCase {
    private static final String EMAIL_CONFIG_KEY="ECMP_EMAIL";
    @Autowired
    private ContentBuilder contentBuilder;
    @Autowired
    private Properties javaMailProperties;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailProcesser emailProcesser;

    @Test
    public void checkBean(){
        Assert.assertNotNull(javaMailProperties);
        System.out.println(JsonUtils.toJson(javaMailProperties));
        Assert.assertNotNull(javaMailSender);
    }

    @Test
    public void getJavaMailProperties(){
        Properties properties = ContextUtil.getGlobalProperties(EMAIL_CONFIG_KEY);
        Assert.assertNotNull(properties);
        System.out.println(JsonUtils.toJson(properties));
    }

    /**
     * 生成消息
     * @return 消息
     */
    private EmailMessage builderMessage(){
        EmailMessage message = new EmailMessage();
        message.setSubject("Test 测试通过模板发送邮件");
        //message.setSender(new EmailAccount("王锦光","wangjg@changhong.com"));
        //测试模板
        message.setContentTemplateCode("EMAIL_TEMPLATE_REGIST");
        Map<String,Object> params = new HashMap<>();
        params.put("userName","宝宝");
        params.put("account","baobao");
        params.put("password","123456");
        message.setContentTemplateParams(params);
        List<EmailAccount> receivers = new ArrayList<>();
        receivers.add(new EmailAccount("王锦光","wangjg@rcsit.cn"));
        receivers.add(new EmailAccount("冯华","hua.feng@changhong.com"));
        message.setReceivers(receivers);
        // 设置消息内容;
        contentBuilder.build(message);
        return message;
    }

    @Test
    public void getMessage(){
        EmailMessage message = builderMessage();
        System.out.println(JsonUtils.toJson(message));
    }

    @Test
    public void send() throws Exception {
        // 发送
        emailProcesser.send(builderMessage());
    }

}