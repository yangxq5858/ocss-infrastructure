package com.ecmp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.hibernate.envers.AuditOverride;
//import org.hibernate.envers.AuditOverrides;

import javax.persistence.*;
import java.util.Date;

/**
 * 实现功能：
 * 业务实体持久化基类
 * 主要的业务实体类(持久化实体和非持久化实体)都应是该类的子类
 * 提供了乐观锁支持和基本字段(创建人，创建时间，最后编辑人和编辑时间)
 *
 * @author Vision.Mac
 * @version 1.0.00      2017/3/12 13:08
 */
@MappedSuperclass
@Access(AccessType.FIELD)
//@AuditOverrides({@AuditOverride(forClass = BaseAuditableEntity.class)})
public abstract class BaseAuditableEntity extends BaseEntity implements IAuditable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @Column(name = "creator_id", length = 36, updatable = false)
    private String creatorId;

    @Column(name = "creator_account", length = 50, updatable = false)
    private String creatorAccount;

    @Column(name = "creator_name", length = 50, updatable = false)
    private String creatorName;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    /**
     * 最后修改者
     */
    @Column(name = "last_editor_id", length = 36)
    private String lastEditorId;

    @Column(name = "last_editor_account", length = 50)
    private String lastEditorAccount;

    @Column(name = "last_editor_name", length = 50)
    private String lastEditorName;

    /**
     * 最后修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_edited_date")
    private Date lastEditedDate;

    @Override
    @JsonIgnore
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    @JsonIgnore
    public String getCreatorAccount() {
        return creatorAccount;
    }

    @Override
    public void setCreatorAccount(String creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    @Override
    @JsonIgnore
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    @JsonIgnore
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    @JsonIgnore
    public String getLastEditorId() {
        return lastEditorId;
    }

    @Override
    public void setLastEditorId(String lastEditorId) {
        this.lastEditorId = lastEditorId;
    }

    @Override
    @JsonIgnore
    public String getLastEditorAccount() {
        return lastEditorAccount;
    }

    @Override
    public void setLastEditorAccount(String lastEditorAccount) {
        this.lastEditorAccount = lastEditorAccount;
    }

    @Override
    @JsonIgnore
    public String getLastEditorName() {
        return lastEditorName;
    }

    @Override
    public void setLastEditorName(String lastEditorName) {
        this.lastEditorName = lastEditorName;
    }

    @Override
    @JsonIgnore
    public Date getLastEditedDate() {
        return lastEditedDate;
    }

    @Override
    public void setLastEditedDate(Date lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }
}
