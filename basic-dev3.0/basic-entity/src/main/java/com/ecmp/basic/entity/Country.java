package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ICodeUnique;
import com.ecmp.core.entity.IRank;
import com.ecmp.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * <p/>
 * 实现功能：国家实体
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Access(AccessType.FIELD)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "country")
public class Country extends BaseAuditableEntity implements ICodeUnique, IRank, ITenant {

    /**
     * 代码
     */
    @Column(name = "code", length = 4, nullable = false, unique = true)
    private String code;

    /**
     * 名称
     */
    @Column(name = "name", length = 60, nullable = false)
    private String name;

    /**
     * 国家货币代码
     */
    @Column(name = "currency_code", length = 5, nullable = false)
    private String currencyCode;

    /**
     * 国家货币名称
     */
    @Column(name = "currency_name", length = 150, nullable = false)
    private String currencyName;

    /**
     * 是否国外
     */
    @Column(name = "to_foreign")
    private Boolean toForeign = Boolean.FALSE;

    /**
     * 排序
     */
    @Column(name = "rank", nullable = false)
    private Integer rank;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false)
    private String tenantCode;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Boolean getToForeign() {
        return toForeign;
    }

    public void setToForeign(Boolean toForeign) {
        this.toForeign = toForeign;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
