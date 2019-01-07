package com.ecmp.edm.manager.dao;

import com.ecmp.edm.entity.BusinessInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：文档业务信息数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-17 11:24      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface BusinessInfoDao extends MongoRepository<BusinessInfo, String> {
    /**
     * 通过业务实体Id获取业务信息
     *
     * @param entityId 业务实体Id
     * @return 业务信息清单
     */
    List<BusinessInfo> findAllByEntityId(String entityId);

    /**
     * 判断文档Id是否存在业务信息
     *
     * @param documentId 文档Id
     * @return 业务信息
     */
    BusinessInfo findFirstByDocumentId(String documentId);
}
