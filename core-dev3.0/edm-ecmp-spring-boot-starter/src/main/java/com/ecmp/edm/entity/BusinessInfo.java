package com.ecmp.edm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：文档的业务信息
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-17 11:08      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Document
@CompoundIndex(def = "{'entityId':1,'documentId':1}", name = "entityId_documentId_index", unique = true)
public class BusinessInfo {
    /**
     * Id标识
     */
    @Id
    private String id;
    /**
     * 业务实体Id
     */
    @Indexed(name = "entityId_index")
    private String entityId;
    /**
     * 文档Id
     */
    private String documentId;

    /**
     * 构造函数
     */
    public BusinessInfo(String entityId, String documentId) {
        this.entityId = entityId;
        this.documentId = documentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
