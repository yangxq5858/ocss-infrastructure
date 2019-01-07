package com.ecmp.notify.service;

import com.ecmp.context.ContextUtil;
import com.ecmp.notity.entity.MessageContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>消息内容生成器</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-11-23 19:38
 */
@Service
public class ContentBuilder {
    private SpringTemplateEngine templateEngine;
    /**
     * 获取模板引擎
     * @return 模板引擎
     */
    private SpringTemplateEngine getTemplateEngine(){
        if(null == templateEngine){
            templateEngine = new SpringTemplateEngine();
            StringTemplateResolver templateResolver =new StringTemplateResolver();
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateEngine.setTemplateResolver(templateResolver);
        }
        return templateEngine;
    }
    /**
     * 生成消息内容
     * @param content 内容
     * @return 消息内容
     */
    public void build(MessageContent content){
        if (Objects.isNull(content)){
            return;
        }
        String templateCode = content.getContentTemplateCode();
        Map<String, Object> templateParams = content.getContentTemplateParams();
        if (StringUtils.isNotBlank(templateCode)) {
            //获取模板内容
            String template = ContextUtil.getGlobalProperty(templateCode);
            if (StringUtils.isNotBlank(template)) {
                //通过模板生成内容
                final Context ctx = new Context(Locale.getDefault());
                if (templateParams != null && !templateParams.isEmpty()) {
                    templateParams.forEach(ctx::setVariable);
                }
                String templateContent = getTemplateEngine().process(template, ctx);
                content.setContent(templateContent);
            }
        }
    }
}
