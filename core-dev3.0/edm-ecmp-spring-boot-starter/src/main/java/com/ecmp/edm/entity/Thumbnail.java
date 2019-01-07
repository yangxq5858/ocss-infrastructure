package com.ecmp.edm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：缩略图
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-14 10:10      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
@Document
public class Thumbnail {
    @Id // 主键
    private String id;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 缩略图数据
     */
    private byte[] image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
