package com.ecmp.edm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>电子文档引用信息</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2018-01-16 11:26
 */
public class DocReferenceInfo implements Serializable{
    /**
     * 业务单据说明
     */
    private String orderNote;
    /**
     * 应用的文档信息清单
     */
    private List<DocumentInfo> documentInfos;

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public List<DocumentInfo> getDocumentInfos() {
        return documentInfos;
    }

    public void setDocumentInfos(List<DocumentInfo> documentInfos) {
        this.documentInfos = documentInfos;
    }
}
