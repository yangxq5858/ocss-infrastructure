package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;

import javax.persistence.*;

import com.ecmp.core.entity.ICodeUnique;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：功能项组Entity定义
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00     2017/4/19 15:52       余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Access(AccessType.FIELD)
@Entity
@Table(name = "feature_group")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FeatureGroup extends BaseAuditableEntity implements ICodeUnique {

    /**
     * 代码
     */
    @Column(length = 30, nullable = false, unique = true)
    private String code;

    /**
     * 名称
     */
    @Column(length = 30, nullable = false)
    private String name;

    /**
     * 应用模块Id
     */
    @ManyToOne
    @JoinColumn(name = "app_module_id", nullable = false)
    private AppModule appModule;

    public AppModule getAppModule() {
        return appModule;
    }

    public void setAppModule(AppModule appModule) {
        this.appModule = appModule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
