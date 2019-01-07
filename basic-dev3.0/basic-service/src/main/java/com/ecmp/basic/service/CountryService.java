package com.ecmp.basic.service;

import com.ecmp.basic.api.ICountryService;
import com.ecmp.basic.dao.CountryDao;
import com.ecmp.basic.dao.RegionDao;
import com.ecmp.basic.entity.Country;
import com.ecmp.basic.entity.vo.CurrencyVo;
import com.ecmp.config.util.ApiClient;
import com.ecmp.core.dao.BaseEntityDao;
import com.ecmp.core.search.PageResult;
import com.ecmp.core.search.Search;
import com.ecmp.core.service.BaseEntityService;
import com.ecmp.vo.OperateResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.GenericType;

/**
 * <p/>
 * 实现功能：国家的业务逻辑
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
@Service
public class CountryService extends BaseEntityService<Country> implements ICountryService {

    @Autowired
    private CountryDao dao;
    @Autowired
    private RegionDao regionDao;

    @Override
    protected BaseEntityDao<Country> getDao() {
        return dao;
    }

    /**
     * 获取货币信息
     *
     * @return 货币信息
     */
    @Override
    public PageResult<CurrencyVo> findCurrency(Search search) {
        GenericType<PageResult<CurrencyVo>> pageResultGenericType = new GenericType<PageResult<CurrencyVo>>() {
        };
        return ApiClient.postViaProxyReturnResult("FIM_API", CurrencyVo.PATH, pageResultGenericType, search);
    }

    /**
     * 删除前检查是否能删除
     *
     * @param s 待删除的国家id
     * @return 操作结果
     */
    @Override
    protected OperateResult preDelete(String s) {
        OperateResult result = null;
        if (regionDao.isExistsByProperty("countryId", s)) {
            //该国家下存在行政区域，禁止删除！
            result = OperateResult.operationFailure("00049");
        }
        return result;
    }

    /**
     * 根据代码查询国家
     * 
     * @param code 代码
     * @return 国家信息
     */
	@Override
	public Country findByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}
		return super.findByProperty(Country.CODE_FIELD, code);
	}
}
