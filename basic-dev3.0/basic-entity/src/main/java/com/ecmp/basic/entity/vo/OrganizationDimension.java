package com.ecmp.basic.entity.vo;

import java.io.Serializable;

/**
 * <p>
 * 实现功能：组织机构维度
 * <p/>
 *
 * @author 秦有宝
 * @version 1.0.00
 */
public class OrganizationDimension implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
