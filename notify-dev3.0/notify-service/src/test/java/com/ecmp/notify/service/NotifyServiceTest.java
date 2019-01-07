package com.ecmp.notify.service;

//import com.ecmp.config.ServiceConfig;
import com.ecmp.notity.entity.EcmpMessage;
import com.ecmp.notity.entity.NotifyType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
 * 1.0.00      2017-06-15 19:42      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class NotifyServiceTest extends BaseContextTestCase{
    @Autowired
    private NotifyService service;

    /**
     * 构造一个平台消息
     * @return
     */
    private EcmpMessage createMessage(){
        EcmpMessage message = new EcmpMessage();
        List<NotifyType> notifyTypes = new ArrayList<>();
        notifyTypes.add(NotifyType.Email);
        message.setNotifyTypes(notifyTypes);
        message.setSenderId("1C67DAA0-3530-11E7-9C56-ACE010C46AFD");
        message.setSubject("测试平台消息发送-test subject");
        message.setContent("测试平台消息发送-test content");
        List<String> receiverIds = new ArrayList<>();
        receiverIds.add("1C67DAA0-3530-11E7-9C56-ACE010C46AFD");
        receiverIds.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        message.setReceiverIds(receiverIds);
        //测试模板
        message.setContentTemplateCode("EMAIL_TEMPLATE_REGIST");
        Map<String,Object> params = new HashMap<>();
        params.put("userName","宝宝");
        params.put("account","baobao");
        message.setContentTemplateParams(params);
        return message;
    }

    /**
     * 构造一个平台消息
     * @return
     */
    private EcmpMessage createWorkMessage(){
        EcmpMessage message = new EcmpMessage();
        List<NotifyType> notifyTypes = new ArrayList<>();
        notifyTypes.add(NotifyType.Email);
        message.setNotifyTypes(notifyTypes);
        message.setSenderId("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        message.setSubject("邮件testNew2:邮件测试new1");
        //message.setContent("测试工作流消息发送-test content");
        List<String> receiverIds = new ArrayList<>();
        receiverIds.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        message.setReceiverIds(receiverIds);
        //测试模板
        message.setContentTemplateCode("EMAIL_TEMPLATE_BEFORE_DOWORK");
        Map<String,Object> params = new HashMap<>();
        params.put("businessCode","D10131");
        params.put("workCaption","sfdasdfa");
        params.put("businessName","邮件测试3");
        params.put("preOpinion","流程启动");
        params.put("remark","2222");
        message.setContentTemplateParams(params);
        message.setCanToSender(true);
        return message;
    }

    @Test
    public void send() throws Exception {
        EcmpMessage message = createMessage();
        service.send(message);
    }

    @Test
    public void sendViaApi() throws Exception {
        EcmpMessage message = createMessage();
//        INotifyService proxy = ApiClient.createProxy(INotifyService.class);
//        proxy.send(message);
    }

    @Test
    public void hello() {
        String helloMsg = service.hello("wangjg");
        System.out.println(helloMsg);
    }
}