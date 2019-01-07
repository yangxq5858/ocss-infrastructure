package com.ecmp.basic.entity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据角色分配关系类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-01 8:10      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DataRoleRelation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据角色Id
     */
    private String dataRoleId;
    /**
     * 数据权限类型Id
     */
    private String dataAuthorizeTypeId;
    /**
     * 权限对象实体Id清单
     */
    private List<String> entityIds;

    public String getDataRoleId() {
        return dataRoleId;
    }

    public void setDataRoleId(String dataRoleId) {
        this.dataRoleId = dataRoleId;
    }

    public String getDataAuthorizeTypeId() {
        return dataAuthorizeTypeId;
    }

    public void setDataAuthorizeTypeId(String dataAuthorizeTypeId) {
        this.dataAuthorizeTypeId = dataAuthorizeTypeId;
    }

    public List<String> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<String> entityIds) {
        this.entityIds = entityIds;
    }
}
