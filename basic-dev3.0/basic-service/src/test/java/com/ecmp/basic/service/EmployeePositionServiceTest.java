package com.ecmp.basic.service;

import com.ecmp.basic.api.IEmployeePositionService;
import com.ecmp.basic.entity.Position;
import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.ApiJsonUtils;
import com.ecmp.vo.OperateResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/12 13:06      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class EmployeePositionServiceTest extends BaseContextTestCase {
    @Autowired
    private EmployeePositionService employeePositionService;

    @Test
    public void testInsertRelationsViaAPI() {
        String employeeId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        List<String > positionIds = new ArrayList<>();
        positionIds.add("c0a80171-5bcd-1066-815b-cd8652870006");
        IEmployeePositionService proxy = ApiClient.createProxy(IEmployeePositionService.class);
        OperateResult result = proxy.insertRelations(employeeId, positionIds);
        Assert.assertTrue(result.successful());
    }

    @Test
    public void testInsertRelations() {

        String employeeId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        List<String > positionIds = new ArrayList<>();
        positionIds.add("c0a80171-5bcd-1066-815b-cd8652870006");
        OperateResult result = employeePositionService.insertRelations(employeeId, positionIds);
        Assert.assertTrue(result.successful());

    }

    @Test
    public void testRemoveRelationsViaAPI() {
        String employeeId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        List<String > positionIds = new ArrayList<>();
        positionIds.add("c0a80171-5bcd-1066-815b-cd8652870006");
        IEmployeePositionService proxy = ApiClient.createProxy(IEmployeePositionService.class);
        OperateResult result = proxy.removeRelations(employeeId, positionIds);
        Assert.assertTrue(result.successful());
    }

    @Test
    public void testRemoveRelations() {
        String employeeId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        List<String > positionIds = new ArrayList<>();
        positionIds.add("c0a80171-5bcd-1066-815b-cd8652870006");
        OperateResult result = employeePositionService.removeRelations(employeeId, positionIds);
        Assert.assertTrue(result.successful());
    }

    /**
     * 测试： 通过父实体Id获取子实体清单
     */
    @Test
    public void testGetChildrenFromParentIdViaAPI() {
        String parentId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        IEmployeePositionService proxy = ApiClient.createProxy(IEmployeePositionService.class);
        List<Position> result = proxy.getChildrenFromParentId(parentId);
        Assert.assertNotNull(result);
    }


    /**
     * 测试： 通过父实体Id获取子实体清单
     */
    @Test
    public void getChildrenFromParentId() {
        String parentId  = "00A0E06A-CAAF-11E7-AA54-0242C0A84202";
        IEmployeePositionService proxy = ApiClient.createProxy(IEmployeePositionService.class);
        List<Position> result = proxy.getChildrenFromParentId(parentId);
        Assert.assertNotNull(result);
        System.out.println(ApiJsonUtils.toJson(result));
    }

    /**
     * 获取未分配的子实体清单
     */
    @Test
    public void testGetUnassignedChildrenViaAPI() {
        String parentId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        IEmployeePositionService proxy = ApiClient.createProxy(IEmployeePositionService.class);
        List<Position> result = proxy.getUnassignedChildren(parentId);
        Assert.assertNotNull(result);
    }

    /**
     * 获取未分配的子实体清单
     */
    @Test
    public void testGetUnassignedChildren() {
        String parentId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        List<Position> result = employeePositionService.getUnassignedChildren(parentId);
        Assert.assertNotNull(result);
    }




}
