package com.ecmp.edm.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：文件名处理工具类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-07 17:01      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class FileNameUtil {

    private static Map<DocumentType, String> docTypeMap;

    static {
        docTypeMap = new HashMap<>();
        docTypeMap.put(DocumentType.Image, "jpg|bmp|gif|png|jpeg");
        docTypeMap.put(DocumentType.Pdf, "pdf");
        docTypeMap.put(DocumentType.Word, "doc|docx");
        docTypeMap.put(DocumentType.Excel, "xls|xlsx|csv");
        docTypeMap.put(DocumentType.Powerpoint, "ppt|pptx");
        docTypeMap.put(DocumentType.Compressed, "zip|rar|7z");
    }

    /**
     * 通过文件名获取后缀名
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    public static String getExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot < 0) {
            return "";
        }
        String extension = fileName.substring(lastIndexOfDot + 1);
        return StringUtils.lowerCase(extension);
    }

    /**
     * 通过文件名获取文档类型
     *
     * @param fileName 文件名
     * @return 文档类型
     */
    public static DocumentType getDocumentType(String fileName) {
        String extension = getExtension(fileName);
        if (StringUtils.isBlank(extension)) {
            return DocumentType.Other;
        }
        for (Map.Entry<DocumentType, String> entry : docTypeMap.entrySet()) {
            DocumentType key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.contains(value, extension)) {
                return key;
            }
        }
        return DocumentType.Other;
    }
}

