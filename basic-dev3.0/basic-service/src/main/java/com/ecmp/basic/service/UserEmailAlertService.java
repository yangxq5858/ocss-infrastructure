package com.ecmp.basic.service;

import com.ecmp.basic.api.IUserEmailAlertService;
import com.ecmp.basic.dao.UserEmailAlertDao;
import com.ecmp.basic.entity.UserEmailAlert;
import com.ecmp.config.util.IgnoreCheckSession;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by WangShuFa on 2018/7/11.
 */
@Service
public class UserEmailAlertService extends BaseEntityService<UserEmailAlert> implements IUserEmailAlertService {
    @Autowired
    private UserEmailAlertDao userEmailAlertDao;

    @Override
    protected BaseEntityDao<UserEmailAlert> getDao() {
        return userEmailAlertDao;
    }

    @Override
    public List<UserEmailAlert> findByUserIds(List<String> userIdS) {
        Search search=new Search();
        search.addFilter(new SearchFilter("userId", userIdS, SearchFilter.Operator.IN));
        List<UserEmailAlert> userEmailAlerts = userEmailAlertDao.findByFilters(search);
        if(Objects.isNull(userEmailAlerts) || userEmailAlerts.isEmpty()){
            return Collections.emptyList();
        }
        List<UserEmailAlert> userEmailAlerts1 = userEmailAlerts.stream().filter(r->r.getHours()>0 || r.getToDoAmount()>0).collect(Collectors.toList());
        return userEmailAlerts1;
    }

    @Override
    @IgnoreCheckSession
    public OperateResult updateLastTimes(List<String> userIds) {
        List<UserEmailAlert> userEmailAlertList=findByUserIds(userIds);
        for(UserEmailAlert userEmailAlert: userEmailAlertList){
            userEmailAlert.setLastTime(new Date());
            super.preUpdate(userEmailAlert);
        }
        userEmailAlertDao.saveAll(userEmailAlertList);
        return OperateResult.operationSuccess();
    }
}
