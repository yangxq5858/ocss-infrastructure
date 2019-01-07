package com.ecmp.basic.entity;

import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.TreeEntity;
import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：系统菜单实体
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/19 16:12              李汶强                  新建
 * 1.0.00      2017/5/10 17:20              高银军                  修改字段
 * <p/>
 * *************************************************************************************************
 */
@Access(AccessType.FIELD)
@Entity
@Table(name = "menu")
@DynamicInsert
@DynamicUpdate
public class Menu extends BaseAuditableEntity implements TreeEntity<Menu> {

    /**
     * 菜单代码
     */
    @Column(name = "code",unique = true, length = 10, nullable = false)
    private String code;
    /**
     * 菜单名称
     */
    @Column(name = "name", length = 20, nullable = false)
    private String name;
    /**
     * 菜单代码路径
     */
    @Column(name = "code_path", length = 500, nullable = false)
    private String codePath;

    /**
     * 菜单名称路径
     */
    @Column(name = "name_path", length = 500, nullable = false)
    private String namePath;

    /**
     * 菜单层级
     */
    @Column(name = "node_level", nullable = false)
    private Integer nodeLevel;

    /**
     * 排序号
     */
    @Column(name = "rank", nullable = false)
    private Integer rank=0;

    /**
     * 父节点id
     */
    @Column(name = "parent_id", length = 36)
    private String parentId;

    /**
     * 关联功能项id
     */
    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

    /**
     * 图标样式名称
     */
    @Column(name = "icon_cls", length = 30)
    private String iconCls;

    /**
     * 子节点列表
     */
    @Transient
    private List<Menu> children;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
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
    public Integer getNodeLevel() {
        return nodeLevel;
    }

    @Override
    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    @Override
    public List<Menu> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
