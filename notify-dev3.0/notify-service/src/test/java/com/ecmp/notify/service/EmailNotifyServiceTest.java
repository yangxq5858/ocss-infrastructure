package com.ecmp.notify.service;

import com.ecmp.config.util.ApiClient;
import com.ecmp.notify.api.IEmailNotifyService;
import com.ecmp.notity.entity.EmailAccount;
import com.ecmp.notity.entity.EmailMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-14 21:23      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class EmailNotifyServiceTest extends BaseContextTestCase{
    @Autowired
    private EmailNotifyService service;

    //创建一个EMAIL消息
    private EmailMessage createMessage(){
        EmailMessage message = new EmailMessage();
        message.setSubject("Test 测试用消息队列通过模板发送邮件");
        message.setSender(new EmailAccount("王锦光","wangjg@changhong.com"));
        List<EmailAccount> receivers = new ArrayList<>();
        receivers.add(new EmailAccount("王锦光","wangjg@rcsit.cn"));
        receivers.add(new EmailAccount("冯华","hua.feng@changhong.com"));
        message.setReceivers(receivers);
        //测试模板
        message.setContentTemplateCode("EMAIL_TEMPLATE_REGIST");
        Map<String,Object> params = new HashMap<>();
        params.put("userName","宝宝");
        params.put("account","baobao");
        params.put("password","123456");
        message.setContentTemplateParams(params);
        //message.setContent("send email use mq[topic:ecmp_email]. 用消息队列发送测试邮件！");
        return message;
    }

    @Test
    public void sendEmail() throws Exception{
        EmailMessage message = createMessage();
        service.sendEmail(message);
        Thread.sleep(10*1000);
    }

    @Test
    public void sendEmailViaApi() throws Exception{
        EmailMessage message = createMessage();
        IEmailNotifyService proxy = ApiClient.createProxy(IEmailNotifyService.class);
        proxy.sendEmail(message);
    }
}