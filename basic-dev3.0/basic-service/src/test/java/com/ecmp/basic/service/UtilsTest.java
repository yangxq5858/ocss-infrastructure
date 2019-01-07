package com.ecmp.basic.service;

import com.ecmp.basic.entity.Corporation;
import com.ecmp.util.DateUtils;
import org.junit.Test;

import java.util.Date;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：静态工具类测试
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-06-19 15:38      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class UtilsTest {
    @Test
    public void printLongToDate(){
        //更新时间戳
        Date date = new Date(1497859860166L);
        System.out.println(DateUtils.formatTime(date));
        Date date2 = new Date(1497859937845L);
        System.out.println(DateUtils.formatTime(date2));
    }

    @Test
    public void stringTest(){
        //--先从缓存中读取
        String entityClassName = Corporation.class.getName();
        String featureCode = null;
        String userId = "8f9f3a92-3f82-11e7-ac6f-005056930c6b";
        String catchKey = entityClassName+"_"+featureCode+"_"+userId;
        System.out.println(catchKey);
    }
}
