package com.ecmp.basic.service;

import com.ecmp.basic.api.IPositionFeatureRoleService;
import com.ecmp.basic.api.IPositionService;
import com.ecmp.basic.entity.Organization;
import com.ecmp.basic.entity.Position;
import com.ecmp.basic.entity.PositionCategory;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.PositionQueryParam;
import com.ecmp.config.util.ApiClient;
import com.ecmp.core.search.*;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;

import java.util.*;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位业务逻辑层测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间                  变更人                 变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/18 9:12            高银军                  新建
 * <p/>
 * *************************************************************************************************
 */
public class PositionServiceTest extends BaseContextTestCase {
    @Test
    public void getUserAuthorizedEntities() throws Exception {
        List<Position> positions = positionService.getUserAuthorizedEntities(null);
        Assert.assertNotNull(positions);
        System.out.println(JsonUtils.toJson(positions));
    }

    @Autowired
    private PositionService positionService;

    @Test
    public void testFindByPage(){
        Search search = new Search();
        //查询字段
        List<SearchFilter> filters = new ArrayList<>();
        SearchFilter filter1 = new SearchFilter();
        filter1.setFieldName("organization.id");
        filter1.setOperator(SearchFilter.Operator.EQ);
        filter1.setValue("c0a80171-5bcd-1066-815b-cd83f5a20002");
        filters.add(filter1);

        //排序字段
        List<SearchOrder> searchOrders = new ArrayList<>();
        SearchOrder searchOrder1 = new SearchOrder("code", SearchOrder.Direction.ASC);
        searchOrders.add(searchOrder1);

        //分页信息
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setRows(15);

        search.setFilters(filters);
        search.setPageInfo(pageInfo);
        search.setSortOrders(searchOrders);
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        PageResult<Position> result = proxy.findByPage(search);
       // PageResult<Position> result = positionService.findByPage(search);
        Assert.assertTrue(result.getRows().size()==1);
    }

    @Test
    public void testFindByIds(){
        List<String> ids = new ArrayList<>();
        ids.add("29961710-410A-11E7-91BA-ACE010C46AFD");
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Position> result = proxy.findByIds(ids);
       // List<Position> result = positionService.findByIds(ids);
        Assert.assertTrue(result.size()==1);
    }

    /**
     * 测试保存岗位实体
     */
    @Test
    public void testSave() {
        Position position = new Position();
        //position.setId("9204D8AB-3C54-11E7-849C-9681B6E77C6A");
        position.setName("测试1222");
        position.setCode("10014");
        PositionCategory positionCategory = new PositionCategory();
        positionCategory.setId("07F93AFF-3B91-11E7-B2C2-9681B6E77C6A");
        Organization org = new Organization();
        org.setId("c0a80171-5bcd-1066-815b-cd83f5a20002");
        position.setPositionCategory(positionCategory);
        position.setOrganization(org);
        OperateResultWithData<Position> resultWithData = positionService.save(position);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 用代理的方法测试保存岗位实体
     */
    @Test
    public void testSaveViaApi() {
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        Position position = new Position();
        position.setName("岗位1");
        position.setTenantCode("10011");
        PositionCategory positionCategory = new PositionCategory();
        positionCategory.setId("c0a80171-5bcd-1066-815b-cd85c58f0004");
        Organization org = new Organization();
        org.setId("c0a80171-5bcd-1066-815b-cd83f5a20002");
        position.setOrganization(org);
        position.setPositionCategory(positionCategory);
        OperateResultWithData<Position> resultWithData = proxy.save(position);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 查询所有的岗位
     */
    @Test
    public void testFindAll() {
        List<Position> positionList = positionService.findAll();
        Assert.assertEquals(1, positionList.size());
    }

    /**
     * 测试查找一个岗位实体
     */
    @Test
    public void testFindOne() {
        Position position = positionService.findOne("c0a80171-5bcd-1066-815b-cd8652870006");
        Assert.assertNotNull(position);
    }

    /**
     * 用代理的方法测试查询一个岗位实体
     */
    @Test
    public void testFindOneViaApi() {
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        Position position = proxy.findOne("c0a80171-5bcd-1066-815b-cd8652870006");
        Assert.assertNotNull(position);
    }

    /**
     * 测试删除一个岗位实体
     */
    @Test
    public void testDelete() {
        OperateResult result = positionService.delete("c0a80167-5b80-16db-815b-8026ebc50000");
        Assert.assertTrue(result.successful());
    }

    /**
     * 用代理的方法测试删除一个岗位实体
     */
    @Test
    public void testDeleteViaApi() {
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        OperateResult result = proxy.delete("c0a80167-5b80-16db-815b-8026ebc50000");
        Assert.assertTrue(result.successful());
    }

    /**
     * 根据岗位类别的id来查询岗位
     */
    @Test
    public void testFindByCategoryId() {
        List<Position> positionList = positionService.findByCategoryId("E06FDD29-5710-11E7-B38B-024288FEC094");
        Assert.assertEquals(1, positionList.size());
    }

    /**
     * 用代理的方式根据岗位类别的id来查询岗位
     */
    @Test
    public void testFindByCategoryIdViaApi() {
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Position> positionList = proxy.findByCategoryId("001");
        Assert.assertEquals(1, positionList.size());
    }

    /**
     * 根据组织机构的id获取岗位
     */
    @Test
    public void testFindByOrganizationId() {
        List<Position> positionList = positionService.findByOrganizationId("141414cd-5b9d-1de9-815b-9dce05050000");
        Assert.assertEquals(1, positionList.size());
    }

    /**
     * 获取岗位
     */
    @Test
    public void findByPositionQueryParam() {
        PositionQueryParam param = new PositionQueryParam();
        param.setIncludeSubNode(true);
        param.setOrganizationId("c0a80171-5bcd-1066-815b-cd8371570001");

        //排序字段
        List<SearchOrder> searchOrders = new ArrayList<>();
        SearchOrder searchOrder1 = new SearchOrder("code", SearchOrder.Direction.ASC);
        searchOrders.add(searchOrder1);
        param.setSortOrders(searchOrders);
        //分页信息
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setRows(15);
        param.setPageInfo(pageInfo);
        //IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        PageResult<Position> result = positionService.findByPositionQueryParam(param);
        System.out.println(result.getRows().size());
    }

    /**
     * 用代理的方式根据组织机构的id获取岗位
     */
    @Test
    public void testFindByOrganizationIdViaApi() {
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Position> positionList = proxy.findByOrganizationId("141414cd-5b9d-1de9-815b-9dce05050000");
        Assert.assertEquals(1, positionList.size());
    }

    /**
     * 根据岗位的id获取执行人
     *
     */
    @Test
    public void getExecutorsByPositionId() {
        String positionId = "c0a80171-5bcd-1066-815b-cd868cd80007";
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Executor> executorList = proxy.getExecutorsByPositionId(positionId);
        Assert.assertNotNull(executorList);
    }

    /**
     * 根据岗位的id列表获取执行人
     *
     */
    @Test
    public void getExecutorsByPositionIds() {
        List<String> positionIds = new ArrayList<>();
        positionIds.add("89A5B0FA-6BDA-11E8-BD32-0A580A8100A7");
        //IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Executor> executorList = positionService.getExecutorsByPositionIds(positionIds);
        Assert.assertNotNull(executorList);
    }

    /**
     * 根据岗位类别的id获取执行人
     */
    @Test
    public void getExecutorsByPosCateId() {
        IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Executor> executorList = proxy.getExecutorsByPosCateId("c0a80171-5bcd-1066-815b-cd85ea570005");
        Assert.assertNotNull(executorList);
    }

    /**
     * 根据岗位类别的id列表获取执行人
     */
    @Test
    public void getExecutorsByPosCateIds() {
        List<String> posCateIds = Arrays.asList("0B6D3CA2-5714-11E7-B38B-024288FEC094","2505D193-5714-11E7-B38B-024288FEC094","D019BC26-5710-11E7-B38B-024288FEC094","D5D1D0D7-5710-11E7-B38B-024288FEC094","DB651F78-5710-11E7-B38B-024288FEC094","E06FDD29-5710-11E7-B38B-024288FEC094","E4786D1A-5710-11E7-B38B-024288FEC094","E934477B-5710-11E7-B38B-024288FEC094","EC4EEDA0-5713-11E7-B38B-024288FEC094","F2D1B7DB-5ADA-11E7-B732-024288FEC094");
        //IPositionService proxy = ApiClient.createProxy(IPositionService.class);
        List<Executor> executorList = positionService.getExecutorsByPosCateIds(posCateIds);
        Assert.assertNotNull(executorList);
    }

    @Test
    public void insertFeatureRoles() {
        String positionId  = "3D4553F6-469E-11E7-BF3E-960F8309DEA7";
        List<String> featureRoleIds = new ArrayList<>();
        featureRoleIds.add("37B9495F-46A4-11E7-A177-960F8309DEA7");
        IPositionFeatureRoleService proxy = ApiClient.createProxy(IPositionFeatureRoleService.class);
        proxy.insertRelations(positionId,featureRoleIds);
    }

}
