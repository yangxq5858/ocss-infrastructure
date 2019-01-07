package com.ecmp.core.entity;

import java.util.Date;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * 特征接口：业务审计属性
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/5/9 15:17      马超(Vision.Mac)                新建
 * <br>
 * *************************************************************************************************
 */
public interface IAuditable {

    /**
     * Returns the user who created this entity.
     *
     * @return the createdBy
     */
    String getCreatorId();

    void setCreatorId(String creatorId);

    String getCreatorAccount();

    void setCreatorAccount(String creatorAccount);

    String getCreatorName();

    void setCreatorName(String creatorName);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    String getLastEditorId();

    void setLastEditorId(String lastEditorId);

    String getLastEditorAccount();

    void setLastEditorAccount(String lastEditorAccount);

    String getLastEditorName();

    void setLastEditorName(String lastEditorName);

    Date getLastEditedDate();

    void setLastEditedDate(Date lastEditedDate);
}
