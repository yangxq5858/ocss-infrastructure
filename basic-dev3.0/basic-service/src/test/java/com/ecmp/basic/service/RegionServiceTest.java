package com.ecmp.basic.service;

import com.ecmp.basic.api.IRegionService;
import com.ecmp.basic.entity.Region;
import com.ecmp.config.util.ApiClient;
import com.ecmp.config.util.ApiJsonUtils;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p/>
 * 实现功能：
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
public class RegionServiceTest extends BaseContextTestCase {

    @Autowired
    private RegionService service;

    @Test
    public void testFindByPage() {
        IRegionService proxy = ApiClient.createProxy(IRegionService.class);
        PageResult<Region> regionPageResult = proxy.findByPage(new Search());
        System.out.println(ApiJsonUtils.toJson(regionPageResult));
        Assert.assertNotNull(regionPageResult);
    }

    @Test
    public void testGetParentPath() {
        Region region = service.findOne("00B643FD-BAB0-47C4-96B1-193575B74661");
        System.out.println(JsonUtils.toJson(region));
        System.out.println(region.getParentPath());
    }

    @Test
    public void save() {
        Region region = new Region();
        region.setCode("马上删");
        region.setName("马上删");
        region.setRank(11);
        region.setCountryId("AB0DB1EA-E0A0-11E7-9901-0242C0A84202");
        region.setParentId("FA61C838-E3B4-11E7-ACB7-74D02BB6F4F2");
        OperateResultWithData<Region> regionOperateResultWithData = service.save(region);
        System.out.println(regionOperateResultWithData.getData());
        Assert.assertTrue(regionOperateResultWithData.successful());
    }

    @Test
    public void delete(){
        OperateResult re;
        re = service.delete("AADA2437-E487-11E7-BC90-74D02BB6F4F2");
        Assert.assertTrue(re.successful());
    }

    @Test
    public void getRegionTreeByCountry() {
        String countryId = "A2D75E62-EA21-4A7C-9E41-7DF0B899F47E";
        List<Region> list = service.getRegionTreeByCountry(countryId);
        System.out.println(ApiJsonUtils.toJson(list));
        Assert.assertNotNull(list);
    }

    @Test
    public void findOne(){
        String id = "3389835E-4FF6-4412-9848-C4DD7DAF45BC";
        Region region = service.findOne(id);
        Assert.assertNotNull(region);
        System.out.println(ApiJsonUtils.toJson(region));
    }
}
