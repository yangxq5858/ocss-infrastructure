package com.ecmp.notity.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：发送电子邮件的消息
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-14 19:47      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class EmailMessage implements Serializable, MessageContent {
    private static final long serialVersionUID = 1L;
    //主题
    private String subject;
    //内容
    private String content;
    //发件人
    private EmailAccount sender;
    //收件人清单
    private List<EmailAccount> receivers;
    /**
     * 内容模板代码
     */
    private String contentTemplateCode;
    /**
     * 内容模板参数
     */
    private Map<String,Object> contentTemplateParams;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailAccount getSender() {
        return sender;
    }

    public void setSender(EmailAccount sender) {
        this.sender = sender;
    }

    public List<EmailAccount> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<EmailAccount> receivers) {
        this.receivers = receivers;
    }

    @Override
    public String getContentTemplateCode() {
        return contentTemplateCode;
    }

    public void setContentTemplateCode(String contentTemplateCode) {
        this.contentTemplateCode = contentTemplateCode;
    }

    @Override
    public Map<String, Object> getContentTemplateParams() {
        return contentTemplateParams;
    }

    public void setContentTemplateParams(Map<String, Object> contentTemplateParams) {
        this.contentTemplateParams = contentTemplateParams;
    }
}
