package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：权限对象类型
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-31 9:34      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "authorize_entity_type")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthorizeEntityType extends BaseAuditableEntity {
    /**
     * 实体类名
     */
    @Column(name = "entity_class_name", length = 100, nullable = false, unique = true)
    private String entityClassName;

    /**
     * 名称
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /**
     * 应用模块
     */
    @ManyToOne
    @JoinColumn(name = "app_module_id", nullable = false)
    private AppModule appModule;

    /**
     * 是树形结构
     */
    @Column(name = "be_tree", nullable = false)
    private Boolean beTree;

    /**
     * API服务路径
     */
    @Column(name = "api_path", length = 100, nullable = false)
    private String apiPath;

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AppModule getAppModule() {
        return appModule;
    }

    public void setAppModule(AppModule appModule) {
        this.appModule = appModule;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public Boolean getBeTree() {
        return beTree;
    }

    public void setBeTree(Boolean beTree) {
        this.beTree = beTree;
    }
}
