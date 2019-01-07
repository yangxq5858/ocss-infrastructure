package com.ecmp.edm.manager.dao;

import com.ecmp.edm.entity.DocumentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：文档信息数据访问接口
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-11 14:10      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Repository
public interface DocumentInfoDao extends MongoRepository<DocumentInfo, String> {
    /**
     * 获取一个时间段的文档信息
     *
     * @param startTime 起始时间
     * @param endTime   截至时间
     * @return 文档信息清单
     */
    List<DocumentInfo> findAllByUploadedTimeBetween(Date startTime, Date endTime);
}
