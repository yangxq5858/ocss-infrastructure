package com.ecmp.basic.entity.vo;

import com.ecmp.basic.entity.Menu;
import com.ecmp.core.entity.TreeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：菜单的展示类view object
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-19 9:12      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class MenuVo implements Serializable, TreeEntity<MenuVo> {
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
     * 排序
     */
    private Integer rank;
    /**
     * 层级
     */
    private Integer nodeLevel;
    /**
     * 父节点Id
     */
    private String parentId;
    /**
     * 代码路径
     */
    private String codePath;
    /**
     * 名称路径
     */
    private String namePath;
    /**
     * 子节点清单
     */
    private List<MenuVo> children;
    /**
     * 功能项资源
     */
    private String featureUrl;
    /**
     * 图标样式
     */
    private String iconCls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code;
    }

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
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public Integer getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
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
    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public String getFeatureUrl() {
        return featureUrl;
    }

    public void setFeatureUrl(String featureUrl) {
        this.featureUrl = featureUrl;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    /**
     * 默认构造函数
     */
    public MenuVo() {
    }

    /**
     * 通过菜单构造
     *
     * @param menu 菜单节点
     */
    public MenuVo(Menu menu) {
        id = menu.getId();
        code = menu.getCode();
        name = menu.getName();
        rank = menu.getRank();
        nodeLevel = menu.getNodeLevel();
        parentId = menu.getParentId();
        codePath = menu.getCodePath();
        namePath = menu.getNamePath();
        iconCls = menu.getIconCls();
    }
}
