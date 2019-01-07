package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ICodeUnique;
import com.ecmp.core.entity.ITenant;
import com.ecmp.core.entity.TreeEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * <p/>
 * 实现功能：行政区域实体
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Access(AccessType.FIELD)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "region")
public class Region extends BaseAuditableEntity implements TreeEntity<Region>, ICodeUnique, ITenant {

    /**
     * 代码
     */
    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    /**
     * 名称
     */
    @Column(name = "name", length = 90, nullable = false)
    private String name;

    /**
     * 代码路径
     */
    @Column(name = "code_path", length = 500, nullable = false)
    private String codePath;

    /**
     * 名称路径
     */
    @Column(name = "name_path", length = 500, nullable = false)
    private String namePath;

    /**
     * 父节点Id
     */
    @Column(name = "parent_id", length = 36)
    private String parentId;

    /**
     * 层级
     */
    @Column(name = "node_level", nullable = false)
    private Integer nodeLevel;

    /**
     * 国家Id
     */
    @Column(name = "country_id", length = 36, nullable = false)
    private String countryId;

    /**
     * 关联国家
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, insertable = false, updatable = false)
    private Country country;

    /**
     * 排序
     */
    @Column(name = "rank", nullable = false)
    private Integer rank;


    /**
     * 缩写
     */
    @Column(name = "short_name", length = 30)
    private String shortName;

    /**
     * 拼音
     */
    @Column(name = "pin_yin", length = 200)
    private String pinYin;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false)
    private String tenantCode;

    /**
     * 子节点列表
     */
    @Transient
    private List<Region> children;

    /**
     * 父节点路径
     */
    @Transient
    private String parentPath;

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

    public void setName(String name) {
        this.name = name;
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
    public Integer getNodeLevel() {
        return nodeLevel;
    }

    @Override
    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    @Override
    public List<Region> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Region> children) {
        this.children = children;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getParentPath() {
        if (getNodeLevel() == null || StringUtils.isBlank(getNamePath()) || StringUtils.isBlank(getName())) {
            return "";
        }
        if (getNodeLevel() == 0) {
            return "";
        }
        return getNamePath().substring(1, getNamePath().length() - getName().length() - 1);
    }
}
