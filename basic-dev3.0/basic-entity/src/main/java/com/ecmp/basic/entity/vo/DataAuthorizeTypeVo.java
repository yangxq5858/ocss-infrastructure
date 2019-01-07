package com.ecmp.basic.entity.vo;

import com.ecmp.basic.entity.DataAuthorizeType;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据权限类型VO
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-01 13:25      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DataAuthorizeTypeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Id标识
     */
    private String id;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 应用模块Id
     */
    private String appModuleId;
    /**
     * 已分配
     */
    private boolean alreadyAssign;
    /**
     * 是树形结构
     */
    private boolean beTree;

    /**
     * 默认构造函数
     */
    public DataAuthorizeTypeVo(){
    }
    /**
     * 构造函数(数据权限类型)
     */
    public DataAuthorizeTypeVo(DataAuthorizeType authorizeType){
        id = authorizeType.getId();
        code = authorizeType.getCode();
        name = authorizeType.getName();
        appModuleId = authorizeType.getAuthorizeEntityType().getAppModule().getId();
        beTree = authorizeType.getAuthorizeEntityType().getBeTree();
        alreadyAssign = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAppModuleId() {
        return appModuleId;
    }

    public void setAppModuleId(String appModuleId) {
        this.appModuleId = appModuleId;
    }

    public boolean isAlreadyAssign() {
        return alreadyAssign;
    }

    public void setAlreadyAssign(boolean alreadyAssign) {
        this.alreadyAssign = alreadyAssign;
    }

    public boolean isBeTree() {
        return beTree;
    }

    public void setBeTree(boolean beTree) {
        this.beTree = beTree;
    }
}
