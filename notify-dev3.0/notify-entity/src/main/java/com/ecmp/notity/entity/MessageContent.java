package com.ecmp.notity.entity;

import java.util.Map;

/**
 * <strong>实现功能:</strong>
 * <p>消息内容接口</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-11-23 18:52
 */
public interface MessageContent {
    /**
     * 消息内容
     * @return 消息内容
     */
    String getContent();
    void setContent(String content);

    /**
     * 内容模板代码
     * @return 模板代码
     */
    String getContentTemplateCode();

    /**
     * 内容模板参数
     * @return 模板参数
     */
    Map<String, Object> getContentTemplateParams();
}
