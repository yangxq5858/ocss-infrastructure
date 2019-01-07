package com.ecmp.basic.service;

import com.ecmp.basic.api.IPositionCategoryService;
import com.ecmp.basic.entity.PositionCategory;
import com.ecmp.config.util.ApiClient;
import com.ecmp.vo.OperateResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：岗位类别服务测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/18 17:05        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
public class PositionCategoryServiceTest extends BaseContextTestCase {
    @Autowired
    PositionCategoryService positionCategoryService;
    @Test
    public void testFindAll(){
        List<PositionCategory> resultWithData = positionCategoryService.findAll();
        System.out.println(resultWithData.size());
    }

    @Test
    public void testFindAllViaApi(){
        IPositionCategoryService proxy = ApiClient.createProxy(IPositionCategoryService.class);
        List<PositionCategory> resultWithData = proxy.findAll();
        System.out.println(resultWithData.size());
    }

    @Test
    public void testFindByIds(){
        List<String> ids = new ArrayList<>();
        ids.add("07F93AFF-3B91-11E7-B2C2-9681B6E77C6A");
        IPositionCategoryService proxy = ApiClient.createProxy(IPositionCategoryService.class);
        List<PositionCategory> result = proxy.findByIds(ids);
        //List<PositionCategory> result = positionCategoryService.findByIds(ids);
        Assert.assertTrue(result.size()==1);
    }

    @Test
    public void testDelete(){

        IPositionCategoryService proxy = ApiClient.createProxy(IPositionCategoryService.class);
        OperateResult result = positionCategoryService.delete("B2101388-4126-11E7-8CA9-960F8309DEA7");
        Assert.assertTrue(result.successful());
        //List<PositionCategory> result = positionCategoryService.findByIds(ids);

    }




}
