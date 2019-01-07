package com.ecmp.basic.service;

import com.ecmp.basic.api.ICorporationService;
import com.ecmp.basic.entity.Corporation;
import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.ApiJsonUtils;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/5 10:20    余思豆(yusidou)                 新建
 * <br>
 * *************************************************************************************************<br>
 */
public class CorporationServiceTest extends BaseContextTestCase {

    @Autowired
    private CorporationService service;
    /**
     * 通过api代理保存一个公司
     */
    @Test
    public void saveApi() {
        ICorporationService porxy = ApiClient.createProxy(ICorporationService.class);
        Corporation corporation = new Corporation();
        corporation.setId("755C456D-5026-11E7-81BF-960F8309DEA7");
        corporation.setCode("10011-002");
        corporation.setName("四川虹信智远");
        corporation.setErpCode("Q000");
        corporation.setRank(1);
        corporation.setFrozen(false);
        corporation.setBaseCurrencyName("人民币");
        corporation.setBaseCurrencyCode("CNY");
        OperateResultWithData<Corporation> resultWithData = porxy.save(corporation);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 本地保存一个公司
     */
    @Test
    public void save() {
        Corporation corporation = new Corporation();
        corporation.setId("5834812B-50C3-11E7-85DA-48E244F5A3DC");
        corporation.setCode("10011-001");
        corporation.setName("四川虹信软件有限公司");
        corporation.setErpCode("Q000");
        corporation.setRank(1);
        corporation.setFrozen(false);
        corporation.setBaseCurrencyCode("C001");
        corporation.setBaseCurrencyName("人民币");
        OperateResultWithData<Corporation> resultWithData = service.save(corporation);
        System.out.println(resultWithData);
        Assert.assertTrue(resultWithData.successful());
    }

    /**
     * 查询所有公司
     */
    @Test
    public void findAll() {
        List<Corporation> corporations = service.findAll();
        Assert.assertNotNull(corporations);
        //输出
        System.out.println(JsonUtils.toJson(corporations));
    }

    /**
     * 查询所有公司
     */
    @Test
    public void findAllByApi() {
        ICorporationService proxy = ApiClient.createProxy(ICorporationService.class);
        List<Corporation> corporations = proxy.findAll();
        Assert.assertNotNull(corporations);
        //输出
        System.out.println(JsonUtils.toJson(corporations));
    }

    /**
     * 查询所有公司(未冻结)
     */
    @Test
    public void findAllUnfrozen() {
        List<Corporation> corporations = service.findAllUnfrozen();
        Assert.assertNotNull(corporations);
        //输出
        System.out.println(JsonUtils.toJson(corporations));
    }

    @Test
    public void findOne(){
        String id = "5834812B-50C3-11E7-85DA-48E244F5A3DC";
        Corporation corporation = service.findOne(id);
        Assert.assertNotNull(corporation);
        //输出
        System.out.println(JsonUtils.toJson(corporation));
    }

    @Test
    public void getUserAuthorizedEntities(){
        List<Corporation> corporations = service.getUserAuthorizedEntities(null);
        Assert.assertNotNull(corporations);
        System.out.println(ApiJsonUtils.toJson(corporations));
    }

    @Test
    public void getUserAuthorizedEntitiesViaApi(){
        ICorporationService proxy = ApiClient.createProxy(ICorporationService.class);
        List<Corporation> corporations = proxy.getUserAuthorizedEntities(null);
        Assert.assertNotNull(corporations);
        System.out.println(ApiJsonUtils.toJson(corporations));
    }
}
