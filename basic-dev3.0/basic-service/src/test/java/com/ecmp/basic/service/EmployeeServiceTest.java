package com.ecmp.basic.service;

import com.ecmp.basic.api.IEmployeeService;
import com.ecmp.basic.api.IPositionService;
import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.Organization;
import com.ecmp.basic.entity.Position;
import com.ecmp.basic.entity.vo.EmployeeQueryParam;
import com.ecmp.basic.entity.vo.Executor;
import com.ecmp.basic.entity.vo.UserQueryParam;
import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.ApiJsonUtils;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.search.*;
import com.ecmp.notify.api.IEmailNotifyService;
import com.ecmp.notity.entity.EmailAccount;
import com.ecmp.notity.entity.EmailMessage;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：员工业务逻辑测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/8 12:00      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class EmployeeServiceTest extends BaseContextTestCase {
    @Autowired
    private EmployeeService service;

    @Test
    public void quickSearch() throws Exception {
        QuickSearchParam param = new QuickSearchParam();
        param.setQuickSearchValue("张");
        PageInfo pageInfo = new PageInfo();
        param.setPageInfo(pageInfo);
        PageResult<Employee> result = service.quickSearch(param);
        Assert.assertNotNull(result);
        System.out.println(ApiJsonUtils.toJson(result));
    }

    @Test
    public void quickSearchExecutors() throws Exception {
        QuickSearchParam param = new QuickSearchParam();
        param.setQuickSearchValue("11");
        PageInfo pageInfo = new PageInfo();
        param.setPageInfo(pageInfo);
        PageResult<Executor> result = service.quickSearchExecutors(param);
        Assert.assertNotNull(result);
        System.out.println(ApiJsonUtils.toJson(result));
    }

    @Test
    public void testSave(){
        //创建
        Organization organization = new Organization();
        organization.setId("825C553C-3FBC-11E7-8570-ACE010C46AFD");
        Employee employee = new Employee();
        employee.setCreateAdmin(false);
        employee.setId("0EE1BBCF-46BC-11E7-BE1A-50465DE22CFB");
        employee.setCode("123456");
        employee.setUserName("小黑");
        employee.setOrganization(organization);
        //修改
//        employee.setId("1582CCF5-33CF-11E7-A2C4-ACE010C46AFD");
//        employee.setCode("20192980");
//        employee.setUserName("小李2");
//        employee.setTenantCode("10011");
//        employee.setOrganizationId("c0a80171-5bcd-1066-815b-cd83f5a20002");

        OperateResultWithData<Employee> resultWithData = service.save(employee);
        Assert.assertTrue(resultWithData.successful());
    }

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
        PageResult<Employee> result = service.findByPage(search);
        Assert.assertTrue(result.getRows().size()==1);
    }

    /**
     * 获取企业员工用户
     */
    @Test
    public void findByUserQueryParam() {
        UserQueryParam param = new UserQueryParam();
        param.setIncludeSubNode(true);
        param.setOrganizationId("9350478C-3B94-11E7-A16A-9681B6E77C6A");

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
        IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        PageResult<Employee> result = proxy.findByUserQueryParam(param);
        System.out.println(result.getRows().size());
    }



    @Test
    public void testFindByEmployeeVo(){
        EmployeeQueryParam search = new EmployeeQueryParam();
        List<String> ids = new ArrayList<>();
        ids.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        search.setPage(4);
        search.setRows(2);
        search.setIds(ids);
        IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        PageResult<Employee> result = service.findByEmployeeParam(search);
        //PageResult<Employee> result = proxy.findByEmployeeParam(search);
        Assert.assertTrue(result.getRows().size()==1);
    }

    /**
     * 测试根据员工的id列表获取员工
     */
    @Test
    public void testFindByIdsViaApi(){
        IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        List<String> ids = new ArrayList<>();
        ids.add("1C67DAA0-3530-11E7-9C56-ACE010C46AFD");
        ids.add("DC0F669A-3BB7-11E7-A34B-ACE010C46AFD");
        List<Employee> result = proxy.findByIds(ids);
        Assert.assertNotNull(result);
    }

    /**
     * 测试根据员工的id列表获取员工
     */
    @Test
    public void testFindByOrganizationIdWithoutFrozen(){
        String idstr = "c0a80171-5bcd-1066-815b-cd83f5a20002";
        List<Employee> result = service.findByOrganizationIdWithoutFrozen(idstr);
        System.out.println(ApiJsonUtils.toJson(result));
    }

    /**
     * 根据组合条件获取员工
     */
    @Test
    public void findByFiltersViaApi(){
        Search searchConfig = new Search();
        searchConfig.addFilter(new SearchFilter("organizationId","c0a80171-5bcd-1066-815b-cd8371570001",SearchFilter.Operator.EQ));
        searchConfig.addSortOrder(new SearchOrder("code", SearchOrder.Direction.ASC));

        IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        List<Employee> result = proxy.findByFilters(searchConfig);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByFilters(){
        Search searchConfig = new Search();
        searchConfig.addFilter(new SearchFilter("organizationId","c0a80171-5bcd-1066-815b-cd8371570001",SearchFilter.Operator.EQ));
        searchConfig.addSortOrder(new SearchOrder("code", SearchOrder.Direction.ASC));

        List<Employee> result = service.findByFilters(searchConfig);
        Assert.assertNotNull(result);
    }
    /**
     * 根据员工的id获取可分配的岗位(获取员工所在部门的岗位)
     */
    @Test
    public void testGetCanAssignedPositionsByEmployeeViaAPI() {
        String employeeId  = "1C67DAA0-3530-11E7-9C56-ACE010C46AFD";
        IEmployeeService employeeProxy = ApiClient.createProxy(IEmployeeService.class);
        IPositionService positionProxy = ApiClient.createProxy(IPositionService.class);
        Employee employee = employeeProxy.findOne(employeeId);
        List<Position> result =  positionProxy.findByOrganizationId(employee.getOrganization().getId());
        Assert.assertNotNull(result);
    }

    /**
     * 测试通过租户代码获取租户管理员
     */
    @Test
    public void testFindAdminByTenantCode(){
        Employee result = service.findAdminByTenantCode("10011");
        Assert.assertNotNull(result);
    }

    /**
     * 测试通过租户代码获取租户管理员
     */
    @Test
    public void testFindAdminByTenantCodeViaApi(){
        IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        Employee result = proxy.findAdminByTenantCode("10011");
        Assert.assertNotNull(result);
    }

    /**
     * 测试通过租户代码获取租户管理员
     */
    @Test
    public void testSaveTenantAdmin(){
        Employee employee = new Employee();
        employee.setCreateAdmin(true);
        employee.setTenantCode("10032");
        Organization organization = new Organization();
        organization.setId("5970F2D8-41C0-11E7-B7D5-960F8309DEA7");
        employee.setOrganization(organization);
        employee.setCode("999999");
        employee.setUserName("秦有宝");
        employee.setEmail("1821519876@qq.com");

        OperateResultWithData<Employee> resultWithData = service.save(employee);
        Assert.assertTrue(resultWithData.successful());
    }

    @Test
    public void sendEmailTest(){
        EmailMessage message = new EmailMessage();
        message.setSubject("企业云平台账号注册成功");
        List<EmailAccount> receivers = new ArrayList<>();
        receivers.add(new EmailAccount("宝","1821519876@qq.com"));
        message.setReceivers(receivers);
        message.setContentTemplateCode("EMAIL_TEMPLATE_REGIST");
        Map<String,Object> params = new HashMap<>();
        params.put("userName", "宝");
        params.put("account", "qinyoubao");
        params.put("password", ContextUtil.getGlobalProperty("ECMP_DEFAULT_USER_PASSWORD"));
        message.setContentTemplateParams(params);

        IEmailNotifyService proxy = ApiClient.createProxy(IEmailNotifyService.class);
        proxy.sendEmail(message);
    }

    @Test
    public void getExecutorsByEmployeeIds(){
        List<String> ids = new ArrayList<>();
        ids.add("A329069C-410B-11E7-91BA-ACE010C46AFD");
        ids.add("0EE1BBCF-46BC-11E7-BE1A-50465DE22CFB");
        IEmployeeService proxy = ApiClient.createProxy(IEmployeeService.class);
        List<Executor> executorList = proxy.getExecutorsByEmployeeIds(ids);
        Assert.assertNotNull(executorList);
    }
}
