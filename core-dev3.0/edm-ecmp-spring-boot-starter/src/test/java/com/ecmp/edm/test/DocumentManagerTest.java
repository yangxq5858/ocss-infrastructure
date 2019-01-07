package com.ecmp.edm.test;

import com.ecmp.edm.entity.Document;
import com.ecmp.edm.entity.DocumentInfo;
import com.ecmp.edm.manager.DocumentManager;
import com.ecmp.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-11 16:58      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class DocumentManagerTest extends UnitTestBase {
    @Autowired(required = false)
    private DocumentManager documentManager;

    @Test
    public void cleanAllDocuments() throws Exception {
        documentManager.cleanAllDocuments();
    }

    @Test
    public void getEntityDocumentInfos() throws Exception {
        String entityId = "123";
        List<DocumentInfo> infos = documentManager.getEntityDocumentInfos(entityId);
        Assert.assertNotNull(infos);
        infos.forEach((i)-> System.out.println(JsonUtils.toJson(i)));
    }

    @Test
    public void submitBusinessInfos() throws Exception {
        String entityId = "C71722C2-6AB0-11E7-B9A7-48E244F5A3DC";
        List<String> docIds = new ArrayList<>();
        docIds.add("D3FC126E-6AB0-11E7-9372-48E244F5A3DA");
        docIds.add("D3FC126E-6AB0-11E7-9372-48E244F5A3DB");
        documentManager.submitBusinessInfos(entityId,docIds);
    }

    @Test
    public void uploadDocument() throws Exception {
        DocumentInfo info = new DocumentInfo();
        info.setFileName("test2.jpg");
        info.setDescription("测试上传文档");
        InputStream stream = new FileInputStream("D:\\TempWork\\Downloads\\2.jpg");
        Document document = new Document(info,stream);
        DocumentInfo documentInfo = documentManager.uploadDocument(document);
        Assert.assertNotNull(documentInfo);
        System.out.println(JsonUtils.toJson(documentInfo));
    }

    @Test
    public void getDocument() throws Exception {
        String id = "5bf2310c3ad251000122adcc";
        Document document = documentManager.getDocument(id,false);
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.getInfo());
        DocumentInfo info = document.getInfo();
        System.out.println("id:"+info.getId());
        System.out.println("filename:"+info.getFileName());
        System.out.println("size:"+info.getFileSize());
        String outFileName = "D:\\TempWork\\Downloads\\out_1.jpg";
        InputStream stream = document.getStream();
        Assert.assertNotNull(stream);
        FileOutputStream file = new FileOutputStream(outFileName);
        byte[] buff = new byte[1024];
        while((stream.read(buff)) != -1){
            file.write(buff);
        }
        stream.close();
        file.close();
    }

    /**
     * 通过 id 获取文件流修改测试
     */
    @Test
    public void getDocumentById(){
        Document document = documentManager.getDocument("5c2c9964e4ca3c4b95425bb3");
        System.out.println(document);
    }
}