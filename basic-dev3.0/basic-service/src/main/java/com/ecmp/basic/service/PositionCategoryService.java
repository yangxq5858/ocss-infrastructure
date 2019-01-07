package com.ecmp.basic.service;

import com.ecmp.basic.api.IPositionCategoryService;
import com.ecmp.basic.dao.PositionCategoryDao;
import com.ecmp.basic.entity.PositionCategory;
import com.ecmp.context.util.NumberGenerator;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：实现岗位类别的业务逻辑服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/17 10:45        秦有宝                      新建
 * <p/>
 * *************************************************************************************************
 */
@Service
public class PositionCategoryService extends BaseEntityService<PositionCategory>
        implements IPositionCategoryService {

    @Autowired
    private PositionCategoryDao positionCategoryDao;
    @Autowired
    private PositionService positionService;

    @Override
    protected BaseEntityDao<PositionCategory> getDao() {
        return positionCategoryDao;
    }

    @Override
    public OperateResultWithData<PositionCategory> save(PositionCategory entity) {
        if(StringUtils.isBlank(entity.getCode())){
            entity.setCode(NumberGenerator.getNumber(PositionCategory.class));
        }
        return super.save(entity);
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param s 待删除数据对象主键
     */
    @Override
    protected OperateResult preDelete(String s) {
        if (positionService.isExistsByProperty("positionCategory.id",s)){
            //该岗位类别下存在岗位，禁止删除！
            return OperateResult.operationFailure("00016");
        }
        return super.preDelete(s);
    }
}
