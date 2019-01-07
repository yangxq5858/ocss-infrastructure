package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.IFrozen;
import com.ecmp.core.entity.ITenant;
import com.ecmp.core.entity.TreeEntity;
import com.ecmp.core.entity.auth.IDataAuthTreeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 15:08        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Access(AccessType.FIELD)
@Entity
@Table(name = "organization")
@DynamicInsert
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization extends BaseAuditableEntity
        implements TreeEntity<Organization>, ITenant, IFrozen, IDataAuthTreeEntity {
    /**
     * 组织机构代码
     */
    @Column(name = "code", length = 6, unique = true, nullable = false)
    private String code;

    /**
     * 组织机构名称
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * 参考码
     */
    @Column(name = "ref_code", length = 50)
    private String refCode;

    /**
     * 层级
     */
    @Column(name = "node_level", nullable = false)
    private Integer nodeLevel;

    /**
     * 代码路径
     */
    @Column(name = "code_path", length = 100, nullable = false)
    private String codePath;

    /**
     * 名称路径
     */
    @Column(name = "name_path", length = 1000, nullable = false)
    private String namePath;

    /**
     * 父节点Id
     */
    @Column(name = "parent_id", length = 36)
    private String parentId;

    /**
     * 排序
     */
    @Column(name = "rank", nullable = false)
    private Integer rank = 0;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false)
    private String tenantCode;

    /**
     * 是否冻结
     */
    @Column(name = "frozen", nullable = false)
    private Boolean frozen = Boolean.FALSE;

    @Transient
    private List<Organization> children;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getNodeLevel() {
        return nodeLevel;
    }

    @Override
    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    @Override
    public String getCodePath() {
        return codePath;
    }

    @Override
    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    @Override
    public String getNamePath() {
        return namePath;
    }

    @Override
    public void setNamePath(String namePath) {
        this.namePath = namePath;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    @Override
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public List<Organization> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Organization> children) {
        this.children = children;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    @Override
    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    /**
     * 预算维度要求实现
     * 预算池显示维度值名称
     */
    @Override
    @Transient
    @JsonProperty
    public String getDisplay() {
        return getName();
    }
}
