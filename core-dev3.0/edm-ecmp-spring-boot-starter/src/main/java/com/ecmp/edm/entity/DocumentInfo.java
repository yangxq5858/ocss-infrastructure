package com.ecmp.edm.entity;

import com.ecmp.util.EnumUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：文档信息
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-10 15:20      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Document
public class DocumentInfo {
    @Id // 主键
    private String id;
    /**
     * 应用模块代码
     */
    private String appModule;
    /**
     * 文件名（包括后缀）
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件描述
     */
    private String description;
    /**
     * 上传时间
     */
    private Date uploadedTime;
    /**
     * 租户代码
     */
    private String tenantCode;
    /**
     * 上传用户Id
     */
    private String uploadUserId;
    /**
     * 上传用户账号
     */
    private String uploadUserAccount;
    /**
     * 上传用户姓名
     */
    private String uploadUserName;
    /**
     * 文件类型
     */
    private Integer documentType;
    /**
     * 文件类型枚举
     */
    private DocumentType documentTypeEnum;
    /**
     * 文件类型枚举名称
     */
    private String documentTypeEnumRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppModule() {
        return appModule;
    }

    public void setAppModule(String appModule) {
        this.appModule = appModule;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(Date uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getUploadUserAccount() {
        return uploadUserAccount;
    }

    public void setUploadUserAccount(String uploadUserAccount) {
        this.uploadUserAccount = uploadUserAccount;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    /**
     * 获取文件大小（K或M）
     *
     * @return
     */
    public String getFileSize() {
        String fileSize;
        long length = size;
        if (size == 0) {
            return "0K";
        }
        if (length < 1024 * 1024) {
            long ksize = length / 1024;
            if (ksize == 0) {
                ksize = 1;
            }
            fileSize = String.format("%dK", ksize);
        } else {
            long msize = length / (1024 * 1024);
            fileSize = String.format("%dM", msize);
        }
        return fileSize;
    }

    public DocumentType getDocumentTypeEnum() {
        return EnumUtils.getEnum(DocumentType.class,documentType);
    }

    public void setDocumentTypeEnum(DocumentType documentTypeEnum) {
        this.documentTypeEnum = documentTypeEnum;
    }

    public String getDocumentTypeEnumRemark() {
        return EnumUtils.getEnumItemRemark(DocumentType.class,documentType);
    }

    public void setDocumentTypeEnumRemark(String documentTypeEnumRemark) {
        this.documentTypeEnumRemark = documentTypeEnumRemark;
    }
}
