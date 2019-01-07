package com.ecmp.basic.service;

import com.ecmp.basic.entity.SupplierUser;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 供应商用户服务测试
 * Author:jamson
 * date:2018/3/8
 */
public class SupplierServiceTest extends BaseContextTestCase {
    @Autowired
    private SupplierUserService supplierService;

    /**
     * 测试新增
     */
    @Test
    public void testSave() {
        SupplierUser supplier = new SupplierUser();
        supplier.setCode("c");
        supplier.setName("供应商用户c");
        OperateResultWithData<SupplierUser> result = supplierService.save(supplier);
        Assert.assertTrue(result.successful());
    }

    /**
     * 测试新增供应商管理员
     */
//    @Test
//    public void testSaveManager() {
//        SupplierUser supplier = new SupplierUser();
//        supplier.setCode("00003");
//        supplier.setName("虹慧云商");
//        supplier.setSupplierId("3F9EE4EF-01BB-11E8-9B36-CA3DD4F003A1");
//        supplier.setSupplierCode(supplier.getCode());
//        supplier.setSupplierName(supplier.getName());
//        OperateResultWithData<SupplierUser> result = supplierService.saveSupplierManager(supplier, "123");
//        Assert.assertTrue(result.successful());
//    }

    /**
     * 测试修改
     */
    @Test
    public void testEdit() {
        String supplierId = "D62C10DA-229A-11E8-9E94-9840BB31885E";
        SupplierUser supplier = supplierService.findOne(supplierId);
        supplier.setCode("b");
        supplier.setName("修改4");
        supplier.setSupplierCode("44");
        OperateResultWithData<SupplierUser> result = supplierService.save(supplier);
        Assert.assertTrue(result.successful());
    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() {
        String supplierId = "07D7D7A6-2289-11E8-85B1-9840BB31885E";
        OperateResult result = supplierService.delete(supplierId);
        Assert.assertTrue(result.successful());
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testFindByPage() {
        PageResult<SupplierUser> pageResult = supplierService.findByPage(new Search());
        ArrayList<SupplierUser> suppliers = pageResult.getRows();
        Assert.assertTrue(!suppliers.isEmpty());
    }

//    /**
//     * 测试冻结
//     */
//    @Test
//    public void testFrozen() {
//        String code = "00040";
//        String supplierId = "9EED9270-28F8-11E8-B6B8-9840BB31885E";
//        boolean frozen = true;
//        OperateResult result = supplierService.freeze(supplierId, code, frozen);
//        Assert.assertTrue(result.successful());
//    }
}
