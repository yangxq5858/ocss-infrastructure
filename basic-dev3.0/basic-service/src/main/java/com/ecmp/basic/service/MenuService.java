package com.ecmp.basic.service;

import com.ecmp.basic.api.IMenuService;
import com.ecmp.basic.dao.MenuDao;
import com.ecmp.basic.entity.Feature;
import com.ecmp.basic.entity.Menu;
import com.ecmp.context.ContextUtil;
import com.ecmp.context.util.NumberGenerator;
import com.ecmp.core.dao.BaseTreeDao;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseTreeService;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：系统菜单服务实现类
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间                  变更人                 变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/4/20 13:44              李汶强                  新建
 * 1.0.00      2017/5/10 17:58              高银军                   修改
 * <br>
 * *************************************************************************************************<br>
 */
@Service
public class MenuService extends BaseTreeService<Menu> implements IMenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    protected BaseTreeDao<Menu> getDao() {
        return menuDao;
    }

    /**
     * 保存菜单项
     *
     * @param menu 要保存的菜单
     * @return 操作后的结果
     */
    @Override
    public OperateResultWithData<Menu> save(Menu menu) {
        if (StringUtils.isBlank(menu.getCode())) {
            menu.setCode(NumberGenerator.getNumber(Menu.class));
        }
        return super.save(menu);
    }

    /**
     * 获取整个Menu多根树的树形对象
     *
     * @return Menu多根树对象集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<Menu> getMenuTree() {
        List<Menu> rootTree = getAllRootNode();
        List<Menu> rootMenuTree = new ArrayList<>();
        for (Menu aRootTree : rootTree) {
            Menu menu = getTree(aRootTree.getId());
            rootMenuTree.add(menu);
        }
        for (Menu menu : rootMenuTree){
            translateBaseAddress(menu);
        }
        return rootMenuTree;
    }

    /**
     * 翻译基地址
     */
    private void translateBaseAddress(Menu menu)
    {
        if (menu == null)
        {
            return;
        }
        if(menu.getFeature() != null){
            if(menu.getFeature().getFeatureGroup()!=null){
                if(menu.getFeature().getFeatureGroup().getAppModule()!=null){
                    String apiBaseAddress = ContextUtil.getGlobalProperty(menu.getFeature().getFeatureGroup().getAppModule().getApiBaseAddress());
                    String webBaseAddress = ContextUtil.getGlobalProperty(menu.getFeature().getFeatureGroup().getAppModule().getWebBaseAddress());
                    menu.getFeature().getFeatureGroup().getAppModule().setApiBaseAddress(apiBaseAddress);
                    menu.getFeature().getFeatureGroup().getAppModule().setWebBaseAddress(webBaseAddress);
                }
            }
        }
        List<Menu> children = menu.getChildren();
        if (children != null && children.size() > 0)
        {
            for (Menu tempMenu : children){
                translateBaseAddress(tempMenu);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> findByNameLike(String name) {
        String nameLike = "%" + name + "%";
        List<Menu> results = menuDao.findByNameLike(nameLike);
        if (!Objects.isNull(results)){
            return results;
        }else {
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    List<Menu> findByFeatures(List<Feature> features){
        if (features==null||features.size()==0){
            return Collections.emptyList();
        }
        List<String> menuFeatureIds = new ArrayList<>();
        features.forEach((f)->{
            if (f.getCanMenu()){
                menuFeatureIds.add(f.getId());
            }
        });
        //通过功能项获取菜单项
        SearchFilter filter = new SearchFilter("feature.id",menuFeatureIds, SearchFilter.Operator.IN);
        return findByFilter(filter);
    }
}
