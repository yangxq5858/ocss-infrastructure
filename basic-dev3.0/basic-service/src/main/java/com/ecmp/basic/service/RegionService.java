package com.ecmp.basic.service;

import com.ecmp.basic.api.IRegionService;
import com.ecmp.basic.dao.RegionDao;
import com.ecmp.basic.entity.Region;
import com.ecmp.context.ContextUtil;
import com.ecmp.core.dao.BaseTreeDao;
import com.ecmp.core.service.BaseTreeService;
import com.ecmp.util.IdGenerator;
import com.ecmp.vo.OperateResultWithData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p/>
 * 实现功能：行政区域的业务逻辑
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Service
public class RegionService extends BaseTreeService<Region> implements IRegionService {

    @Autowired
    private RegionDao dao;

    @Override
    @Transactional(readOnly = true)
    public List<Region> getRegionTree() {
        List<Region> rootTree = getAllRootNode();
        List<Region> rootRegionTree = new ArrayList<>();
        for (Region aRootTree : rootTree) {
            Region region = getTree(aRootTree.getId());
            rootRegionTree.add(region);
        }
        return rootRegionTree;
    }

    /**
     * 通过国家id查询行政区域树
     *
     * @param countryId 国家id
     * @return 行政区域树清单
     */
    @Override
    public List<Region> getRegionTreeByCountry(String countryId) {
        Region region = dao.findByCountryIdAndNodeLevel(countryId, 0);
        return buildTree(dao.findByCodePathStartingWithAndIdNot(region.getCodePath(), region.getId()));
    }

    @Override
    public List<Region> getProvinceByCountry(String countryId) {
        if (StringUtils.isEmpty(countryId)) {
            return Collections.emptyList();
        }
        Region region = dao.findByCountryIdAndNodeLevel(countryId, 0);
        if (region == null) {
            return Collections.emptyList();
        }
        return dao.findListByProperty("parentId", region.getId());
    }

    @Override
    public List<Region> getCityByProvince(String provinceId) {
        if (StringUtils.isEmpty(provinceId)) {
            return Collections.emptyList();
        }
        return dao.findListByProperty("parentId", provinceId);
    }

    @Override
    protected BaseTreeDao<Region> getDao() {
        return dao;
    }


    /**
     * 保存前检查代码唯一性
     *
     * @param entity 待保存的行政区域
     * @return 操作结果
     */
    @Override
    public OperateResultWithData<Region> save(Region entity) {
        String id = IdGenerator.uuid();
        if (StringUtils.isNotBlank(entity.getId())) {
            id = entity.getId();
        }
        if (dao.isCodeExists(ContextUtil.getTenantCode(), entity.getCode(), id)) {
            //该行政区域代码已存在，不能重复！
            return OperateResultWithData.operationFailureWithData(entity, "00050");
        }
        return super.save(entity);
    }
}
