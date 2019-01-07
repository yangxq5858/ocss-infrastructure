package com.ecmp.basic.entity;

import com.ecmp.basic.entity.enums.FeatureType;
import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ICodeUnique;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：功能项实体
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间                  变更人                 变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/4/19 15:12              李汶强                  新建
 * 1.0.00      2017/5/10 17:20              高银军                  修改字段
 * <br>
 * *************************************************************************************************<br>
 */
@Access(AccessType.FIELD)
@Entity
@Table(name = "feature")
@DynamicUpdate
@DynamicInsert
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Feature extends BaseAuditableEntity implements ICodeUnique {

    /**
     * 功能项代码
     */
    @Column(name = "code",unique = true, length = 50, nullable = false)
    private String code;

    /**
     * 功能项名称
     */
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    /**
     * 资源
     */
    @Column(name = "url", length = 400)
    private String url;

    /**
     * 是否菜单项
     */
    @Column(name = "can_menu")
    private Boolean canMenu;

    /**
     * 功能项类型：0：操作(Operate),1：业务(Business)
     */
    @Enumerated
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    /**
     * 功能项组
     */
    @ManyToOne
    @JoinColumn(name = "feature_group_id",nullable = false)
    private FeatureGroup featureGroup;

    /**
     * 租户可用
     */
    @Column(name = "tenant_can_use",nullable = false)
    private Boolean tenantCanUse;

    /**
     * 应用模块名称
     */
    @Transient
    private String appModuleName;
    /**
     * 功能项备注
     */
    @Transient
    private String featureTypeRemark;

    public Boolean getTenantCanUse() {
        return tenantCanUse;
    }

    public void setTenantCanUse(Boolean tenantCanUse) {
        this.tenantCanUse = tenantCanUse;
    }

    public String getFeatureTypeRemark() {
        return featureTypeRemark;
    }

    public void setFeatureTypeRemark(String featureTypeRemark) {
        this.featureTypeRemark = featureTypeRemark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getCanMenu() {
        return canMenu;
    }

    public void setCanMenu(Boolean canMenu) {
        this.canMenu = canMenu;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    public FeatureGroup getFeatureGroup() {
        return featureGroup;
    }

    public void setFeatureGroup(FeatureGroup featureGroup) {
        this.featureGroup = featureGroup;
    }

    public String getAppModuleName() {
        return appModuleName;
    }

    public void setAppModuleName(String appModuleName) {
        this.appModuleName = appModuleName;
    }
}
