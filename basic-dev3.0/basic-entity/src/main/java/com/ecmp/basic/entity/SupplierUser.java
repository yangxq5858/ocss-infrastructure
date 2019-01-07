package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.ICodeUnique;
import com.ecmp.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/3/6 20:03
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "supplier")
@DynamicUpdate
@DynamicInsert
public class SupplierUser extends BaseAuditableEntity implements ITenant, ICodeUnique {

    public static final String SUPPLIER_ID = "supplierId";
    public static final String SUPPLIER_APPLY_ID = "supplierApplyId";
    /**
     * 代码
     */
    @Column(name = "code", length = 30)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name", length = 100)
    private String name;
    /**
     * 供应商id
     */
    @Column(name = "supplier_id", length = 36)
    private String supplierId;
    /**
     * 申请注册供应商ID
     *
     */
    @Column(name = "supplier_apply_id", length = 36)
    private String supplierApplyId;
    /**
     * 供应商代码
     */
    @Column(name = "supplier_code", length = 30)
    private String supplierCode;
    /**
     * 供应商名称
     */
    @Column(name = "supplier_name", length = 100)
    private String supplierName;

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 10, nullable = false, unique = true)
    private String tenantCode;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSupplierApplyId() {
        return supplierApplyId;
    }

    public void setSupplierApplyId(String supplierApplyId) {
        this.supplierApplyId = supplierApplyId;
    }

    @Override
    public String toString() {
        return "SupplierUser{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierApplyId='" + supplierApplyId + '\'' +
                ", supplierCode='" + supplierCode + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", tenantCode='" + tenantCode + '\'' +
                ", user=" + user +
                '}';
    }
}
