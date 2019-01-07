package com.ecmp.basic.service;

import com.ecmp.basic.api.IMenuService;
import com.ecmp.basic.entity.Menu;
import com.ecmp.config.util.ApiClient;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：测试系统菜单的API服务实现类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/20 13:46              李汶强                  新建
 * <p/>
 * *************************************************************************************************
 */
public class MenuServiceTest extends BaseContextTestCase {
    @Test
    public void buildTree() throws Exception {
        List<Menu> nodes = menuService.findAll();
        List<Menu> trees = MenuService.buildTree(nodes);
        Assert.assertNotNull(trees);
        //输出
        System.out.println(JsonUtils.toJson(trees));
    }

    @Autowired
    private MenuService menuService;

    /**
     * 测试获取整个菜单树
     */
    @Test
    public void testGetMenuTree(){
        List<Menu> menuTree = menuService.getMenuTree();
        System.out.println("menuTree.size= "+ menuTree.size());
        for (int i = 0; i < menuTree.size(); i++){
            System.out.println("test" + menuTree.get(i).getChildren().size());
        }
    }

    /**
     * 代理获取整个菜单树
     */
    @Test
    public void testGetMenuTreeViaApi(){
        IMenuService proxy = ApiClient.createProxy(IMenuService.class);
        List<Menu> menuTree = proxy.getMenuTree();
        System.out.println(menuTree.size());
    }

    /**
     * 保存菜单
     */
    @Test
    public void testSave(){
//        Menu menu = new Menu();
//        menu.setName("基础应用");
//        menu.setRank(1);
//        menu.setIconCls("red");
//        OperateResultWithData<Menu> result = menuService.save(menu);
//        Assert.assertTrue(result.successful());
    }

    /**
     * 保存菜单
     */
    @Test
    public void testSaveViaAPi(){
//            Menu menu = new Menu();
//            menu.setId("7EA4B6A8-3AA8-11E7-AD92-9681B6E77C6A");
//            menu.setParentId("67681078-3AA4-11E7-8B44-9681B6E77C6A");
//            menu.setName("test");
//            menu.setRank(10);
//            menu.setIconCls("red");
//            IMenuService proxy = ApiClient.createProxy(IMenuService.class);
//            OperateResultWithData<Menu> result = proxy.save(menu);
//            Assert.assertTrue(result.successful());
    }

    /**
     * 名称关键字模糊查询
     */
    @Test
    public void testSearchByNameLike(){
        List<Menu> results = menuService.findByNameLike("管理");

        System.out.println(results.size());
    }

    @Test
    public void testMove(){
        String nodeId = "D94F57EE-3B8E-11E7-8A24-9681B6E77C6A";
        String targetParentId = "04497FD8-3B8E-11E7-8A24-9681B6E77C6A";

        OperateResult result = menuService.move(nodeId, targetParentId);
        Assert.assertTrue(result.successful());
    }


//    /**
//     * 模糊查询
//     */
//    @Test
//    public void testFindByNameLike() {
//        List<Menu> menus = menuService.findByNameLike("menu%");
//        System.out.println(menus.size());
//    }
//
//    /**
//     * 测试获取所有的菜单
//     */
//    @Test
//    public void testFindAll() {
//        List<Menu> menus = menuService.findAll();
//        Assert.assertEquals(1, menus.size());
//    }
//
//    /**
//     * 通过Api代理测试获取所有的菜单
//     */
//    @Test
//    public void testFindAllViaApi() {
//        IMenuService proxy = ApiClient.createProxy(IMenuService.class);
//        List<Menu> menus = proxy.findAll();
//        Assert.assertEquals(1, menus.size());
//    }
//
//    /**
//     * 测试保存一个菜单
//     */
//    @Test
//    public void testSave() {
////        Menu menu = new Menu();
//        Menu menu = menuService.findOne("B8A003BD-361F-11E7-9703-A86BAD058F5B");
//        menu.setName("menu003");
//        menu.setCodePath("codepath3");
//        menu.setNamePath("namepath3");
//        menu.setNodeLevel(3);
//        menu.setRank(3);
//        menu.setParentId("96C92EF8-3620-11E7-A502-A86BAD058F5B");
//        OperateResultWithData<Menu> resultWithData = menuService.save(menu);
//        System.out.println(resultWithData.getMessage());;
//    }
//
//    /**
//     * 通过Api代理测试保存一个菜单
//     */
//    @Test
//    public void testSaveViaApi() {
//        IMenuService proxy = ApiClient.createProxy(IMenuService.class);
//        Menu menu = new Menu();
//        menu.setId(UUID.randomUUID().toString());
//        menu.setCode("Code3");
//        menu.setName("menu003");
//        menu.setCodePath("111113");
//        menu.setNamePath("222224");
//        menu.setParentId("123");
//
//        OperateResultWithData<Menu> resultWithData = proxy.save(menu);
//
//        Assert.assertTrue(resultWithData.successful());
//    }
//
//    /**
//     * 测试根据id查找一个菜单
//     */
//    @Test
//    public void testFindOne() {
//        Menu menu = menuService.findOne("96C92EF8-3620-11E7-A502-A86BAD058F5B");
//        Assert.assertNotNull(menu);
//    }
//
//    /**
//     * 通过Api代理测试根据id查找一个菜单
//     */
//    @Test
//    public void testFindOneViaApi() {
//        IMenuService proxy = ApiClient.createProxy(IMenuService.class);
//        Menu menu = proxy.findOne("B8A003BD-361F-11E7-9703-A86BAD058F5B");
//        System.out.println(menu.getId());
//        Assert.assertNotNull(menu);
//    }
//
//    /**
//     * 测试菜单移动
//     */
//    @Test
//    public void testMove() {
//        String nodeId = "c0a8016b-5ba4-1f13-815b-a41f31230003";
//        String targetParentId = "c0a8016d-5ba4-1a08-815b-a40a24270001";
//        OperateResult operateResult = menuService.move(nodeId, targetParentId);
//        Assert.assertTrue(operateResult.successful());
//    }
//
//    /**
//     * 测试删除节点
//     */
//    @Test
//    public void testDeleteTreeNode() {
//        OperateResult operateResult = menuService.delete("96C92EF8-3620-11E7-A502-A86BAD058F5B");
//        Assert.assertTrue(operateResult.successful());
//    }
}
