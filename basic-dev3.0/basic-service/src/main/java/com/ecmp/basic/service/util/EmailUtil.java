package com.ecmp.basic.service.util;

import com.ecmp.basic.entity.Employee;
import com.ecmp.config.util.ApiClient;
import com.ecmp.context.ContextUtil;
import com.ecmp.notify.api.IEmailNotifyService;
import com.ecmp.notity.entity.EmailAccount;
import com.ecmp.notity.entity.EmailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实现功能：发送邮件工具类
 * <p/>
 *
 * @author 秦有宝
 * @version 1.0.00
 */
@EnableAsync
@Service
public class EmailUtil {

    /**
     *  构造注册成功后发送的邮件
     * @param employee 注册的员工用户
     * @return 电子邮件的消息
     */
    public EmailMessage constructEmailMessage(Employee employee) {
        EmailMessage message = new EmailMessage();
        message.setSubject("企业云平台账号注册成功");
        List<EmailAccount> receivers = new ArrayList<>();
        receivers.add(new EmailAccount(employee.getUserName(), employee.getEmail()));
        message.setReceivers(receivers);
        message.setContentTemplateCode("EMAIL_TEMPLATE_REGIST");
        Map<String, Object> params = new HashMap<>();
        params.put("userName", employee.getUserName());
        params.put("account", employee.getCode());
        params.put("password", ContextUtil.getGlobalProperty("ECMP_DEFAULT_USER_PASSWORD"));
        message.setContentTemplateParams(params);
        return message;
    }

    /**
     * 注册成功后发送邮件
     *
     * @param message 注册的员工用户
     */
    @Async
    public void sendEmailNotifyUser(EmailMessage message) {
        IEmailNotifyService proxy = ApiClient.createProxy(IEmailNotifyService.class);
        proxy.sendEmail(message);
    }
}
