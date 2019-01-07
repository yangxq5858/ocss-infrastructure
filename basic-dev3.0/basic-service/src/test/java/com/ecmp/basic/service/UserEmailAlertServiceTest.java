package com.ecmp.basic.service;

import com.ecmp.basic.entity.UserEmailAlert;
import com.ecmp.basic.service.UserEmailAlertService;
import com.ecmp.basic.service.UtilsTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangShuFa on 2018/7/11.
 */
public class UserEmailAlertServiceTest extends UtilsTest {
     @Autowired
     private UserEmailAlertService userEmailAlertService;

    @Test
    public void testSave(){
        UserEmailAlert userEmailAlert=new UserEmailAlert();
        userEmailAlert.setUserId("123");
        userEmailAlert.setToDoAmount(3);
        userEmailAlertService.save(userEmailAlert);
    }

    @Test
     public void  testUpdateByUserIds(){
        List<String> userIdList=new ArrayList<>();
        userIdList.add("123");
        List<UserEmailAlert>userEmailAlertList= userEmailAlertService.findByUserIds(userIdList);
        System.out.print(userEmailAlertList);
        userEmailAlertService.updateLastTimes(userIdList);
    }

}
