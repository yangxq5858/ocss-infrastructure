package com.ecmp.basic.entity;

import com.ecmp.core.entity.*;
import com.ecmp.core.entity.auth.IDataAuthEntity;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：公司实体
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/2 16:40    余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
@Access(AccessType.FIELD)
@Entity
@Table(name = "corporation")
@DynamicUpdate
@DynamicInsert
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Corporation extends BaseAuditableEntity
        implements ITenant,ICodeUnique,IRank,IFrozen,IDataAuthEntity {

    /**
     * 名称
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * 代码
     */
    @Column(length = 20, nullable = false, unique = true)
    private String code;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false)
    private String tenantCode;

    /**
     * ERP公司代码
     */
    @Column(name = "erp_code",length = 10, nullable = false)
    private String erpCode;

    /**
     * 排序
     */
    @Column(name = "rank",nullable = false)
    private Integer rank = 0;

    /**
     * 冻结标志
     */
    @Column(name = "frozen",nullable = false)
    private Boolean frozen = Boolean.FALSE;

    /**
     * 本位币货币代码
     */
    @Column(name = "base_currency_code", length = 5, nullable = false)
    private String baseCurrencyCode;

    /**
     * 本位币货币名称
     */
    @Column(name = "base_currency_name", length = 40, nullable = false)
    private String baseCurrencyName;

    /**
     * 默认贸易伙伴代码
     */
    @Column(name = "default_trade_partner", length = 10)
    private String defaultTradePartner;

    /**
     * 关联交易贸易伙伴
     */
    @Column(name = "related_trade_partner", length = 10)
    private String relatedTradePartner;

    /**
     * 微信用户凭证
     */
    @Column(name = "weixin_appid", length = 50)
    private String weixinAppid;

    /**
     * 微信用户凭证密钥
     */
    @Column(name = "weixin_secret", length = 100)
    private String weixinSecret;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public String getDefaultTradePartner() {
        return defaultTradePartner;
    }

    public void setDefaultTradePartner(String defaultTradePartner) {
        this.defaultTradePartner = defaultTradePartner;
    }

    public String getRelatedTradePartner() {
        return relatedTradePartner;
    }

    public void setRelatedTradePartner(String relatedTradePartner) {
        this.relatedTradePartner = relatedTradePartner;
    }

    public String getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(String weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public String getWeixinSecret() {
        return weixinSecret;
    }

    public void setWeixinSecret(String weixinSecret) {
        this.weixinSecret = weixinSecret;
    }
}
