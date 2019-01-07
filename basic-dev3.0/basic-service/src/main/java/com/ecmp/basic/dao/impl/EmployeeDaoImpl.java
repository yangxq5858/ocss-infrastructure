package com.ecmp.basic.dao.impl;

import com.ecmp.basic.dao.EmployeeExtDao;
import com.ecmp.basic.entity.Employee;
import com.ecmp.basic.entity.vo.EmployeeQueryParam;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.impl.BaseEntityDaoImpl;
import com.ecmp.core.entity.IAuditable;
import com.ecmp.core.entity.ITenant;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：企业员工扩展接口实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/26 13:58      秦有宝                     新建
 * <p/>
 * *************************************************************************************************
 */
public class EmployeeDaoImpl extends BaseEntityDaoImpl<Employee> implements EmployeeExtDao {

    public EmployeeDaoImpl(EntityManager entityManager) {
        super(Employee.class, entityManager);
    }

    /**
     * 保存企业员工用户
     *
     * @param entity 企业员工用户实体
     * @param isNew  是否是创建
     * @return 保存结果
     */
    @Override
    @Transactional
    public Employee save(Employee entity, boolean isNew) {
        //是否含有业务审计属性实体
        if (entity != null) {
            Date now = new Date();
            String userId = ContextUtil.getUserId();
            String userAccount = ContextUtil.getUserAccount();
            String userName = ContextUtil.getUserName();
            if (isNew) {//创建
                entity.setCreatorId(userId);
                entity.setCreatorName(userName);
                entity.setCreatorAccount(userAccount);
                entity.setCreatedDate(now);
            }
            entity.setLastEditorId(userId);
            entity.setLastEditorName(userName);
            entity.setLastEditorAccount(userAccount);
            entity.setLastEditedDate(now);
        }
        //是否是租户实体(只是在租户代码为空时设置)
        if (entity != null && StringUtils.isBlank(((ITenant) entity).getTenantCode())) {
            //从上下文中获取租户代码
            ITenant tenant = (ITenant) entity;
            tenant.setTenantCode(ContextUtil.getTenantCode());
        }
        if (isNew) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    /**
     * 根据查询参数获取企业员工(分页)
     *
     * @param employeeQueryParam 查询参数
     * @return 企业员工
     */
    @Override
    public PageResult<Employee> findByEmployeeParam(EmployeeQueryParam employeeQueryParam) {
        if (employeeQueryParam ==null){
            return new PageResult<>();
        }
        boolean idsNotEmpty = !CollectionUtils.isEmpty(employeeQueryParam.getIds());
        PageResult<Employee> pageResult = new PageResult<>();
        String sql = "select e from Employee e inner join User u on u.id=e.id where e.tenantCode=:tenantCode and u.frozen=0";
        if(idsNotEmpty){
            sql += " and e.id not in (:ids)";
        }
        Query query = entityManager.createQuery(sql);
        query.setParameter("tenantCode",ContextUtil.getTenantCode());
        if(idsNotEmpty){
            query.setParameter("ids", employeeQueryParam.getIds());
        }
        //设置总条数
        int totaltRecords = query.getResultList().size();
        int totalPages = employeeQueryParam.getRows() == 0 ? 1 : (int) Math.ceil((double) totaltRecords / (double) employeeQueryParam.getRows());
        pageResult.setRecords(totaltRecords);
        pageResult.setTotal(totalPages);
        query.setFirstResult((employeeQueryParam.getPage()-1)* employeeQueryParam.getRows());
        query.setMaxResults(employeeQueryParam.getRows());
        List<Employee> datas = new ArrayList<>();
        for (Object e: query.getResultList()
             ) {
            datas.add((Employee)e);
        }
        pageResult.setPage(employeeQueryParam.getPage());
        pageResult.setRows(datas);
        return pageResult;
    }

    /**
     * 检查员工编号是否存在
     *
     * @param code 员工编号
     * @param id 实体id
     * @return 是否存在
     */
    @Override
    public Boolean isCodeExist(String code, String id) {
        if(StringUtils.isBlank(id)){
            id = IdGenerator.uuid();
        }
        String sql = "select r.id from Employee r " +
                "where r.code=:code " +
                "and r.tenantCode=:tenantCode " +
                "and r.id<>:id ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("code",code);
        query.setParameter("id",id);
        query.setParameter("tenantCode",ContextUtil.getTenantCode());
        List results = query.getResultList();
        return !results.isEmpty();
    }
}

