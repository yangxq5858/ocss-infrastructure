package com.ecmp.basic.service;

import com.ecmp.basic.api.IDataAuthorizeTypeService;
import com.ecmp.basic.dao.DataAuthorizeTypeDao;
import com.ecmp.basic.entity.DataAuthorizeType;
import com.ecmp.basic.entity.vo.DataAuthorizeTypeVo;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.Search;
import com.ecmp.core.search.SearchFilter;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：数据权限类型业务逻辑实现
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-05-04 14:04      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class DataAuthorizeTypeService extends BaseEntityService<DataAuthorizeType>
        implements IDataAuthorizeTypeService {
    @Autowired
    private DataAuthorizeTypeDao dao;
    @Override
    protected BaseEntityDao<DataAuthorizeType> getDao() {
        return dao;
    }
    @Autowired
    private DataRoleAuthTypeValueService dataRoleAuthTypeValueService;
    @Autowired
    private TenantAppModuleService tenantAppModuleService;

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (dataRoleAuthTypeValueService.isExistsByProperty("dataAuthorizeType.id",s)){
            //数据权限类型存在数据角色分配权限类型的值，禁止删除！
            return OperateResult.operationFailure("00020");
        }
        return super.preDelete(s);
    }

    /**
     * 通过数据角色Id获取数据权限类型（VO）
     *
     * @param roleId 数据角色Id
     * @return 数据权限类型
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataAuthorizeTypeVo> getByDataRole(String roleId) {
        List<String> appModuleIds = tenantAppModuleService.getTenantAppModuleIds();
        List<DataAuthorizeType> authorizeTypes = dao.findByAuthorizeEntityTypeAppModuleIdIn(appModuleIds);
        List<DataAuthorizeTypeVo> result = new ArrayList<>();
        if (authorizeTypes==null||authorizeTypes.size()==0){
            return result;
        }
        authorizeTypes.forEach((t)->{
            DataAuthorizeTypeVo vo = new DataAuthorizeTypeVo(t);
            vo.setAlreadyAssign(dataRoleAuthTypeValueService.isAlreadyAssign(roleId,t.getId()));
            result.add(vo);
        });
        return result;
    }

    /**
     * 通过数据角色Id获取数据权限类型（VO）
     *
     * @param appModuleId 应用模块Id
     * @param roleId      数据角色Id
     * @return 数据权限类型
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataAuthorizeTypeVo> getByAppModuleAndDataRole(String appModuleId, String roleId) {
        List<DataAuthorizeType> authorizeTypes = dao.findByAuthorizeEntityTypeAppModuleId(appModuleId);
        List<DataAuthorizeTypeVo> result = new ArrayList<>();
        if (authorizeTypes==null||authorizeTypes.size()==0){
            return result;
        }
        authorizeTypes.forEach((t)->{
            DataAuthorizeTypeVo vo = new DataAuthorizeTypeVo(t);
            vo.setAlreadyAssign(dataRoleAuthTypeValueService.isAlreadyAssign(roleId,t.getId()));
            result.add(vo);
        });
        return result;
    }

    /**
     * 通过实体类型名和功能项代码获取数据权限类型
     * @param entityClassName 实体类型名
     * @param featureCode 功能项代码
     * @return 数据权限类型
     */
    @Transactional(readOnly = true)
    DataAuthorizeType getByEntityClassNameAndFeature(String entityClassName, String featureCode){
        Search search = new Search();
        search.addFilter(new SearchFilter("authorizeEntityType.entityClassName",entityClassName, SearchFilter.Operator.EQ));
        search.addFilter(new SearchFilter("feature.code",featureCode, SearchFilter.Operator.EQ));
        List<DataAuthorizeType> result = findByFilters(search);
        if (result==null||result.isEmpty()){
            return null;
        }
        return result.get(0);
    }
}
